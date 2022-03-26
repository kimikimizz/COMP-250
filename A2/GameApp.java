import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * THIS CLASS IS NOT INTENDED FOR STUDENTS TO UNDERSTAND.
 * IT PROVIDES A VISUALIZER OF THE GAME!
 */
public class GameApp {
    static Game g;
    static ChessEngine canvas;
    private static final TextField oriX = new TextField();
    private static final TextField oriY = new TextField();
    private static final TextField desX = new TextField();
    private static final TextField desY = new TextField();
    private static final Label statusLabel = new Label("--Welcome to Chess--");
    private static final Label turn = new Label("Turn: WHITE");


    public static void main(String[] args){
        g = new Game();
        JFrame frame = new JFrame(Game.getName());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored){ }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel lower = new JPanel();
        lower.setLayout(new GridLayout(2,1));

        JPanel status = new JPanel();
        status.setLayout(new FlowLayout());
        status.add(turn);
//         status.add(new Label("Status: "));
        status.add(statusLabel);
        statusLabel.setVisible(false);
        frame.add(status, BorderLayout.PAGE_END);
        lower.add(status);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());
//        controls.add(new Label("Origin X: "));
//        controls.add(oriX);
//        oriX.setColumns(2);
//        controls.add(new Label("Origin Y: "));
//        controls.add(oriY);
//        oriY.setColumns(2);
//        controls.add(new Label("Dest X: "));
//        controls.add(desX);
//        desX.setColumns(2);
//        controls.add(new Label("Dest Y: "));
//        controls.add(desY);
//        desY.setColumns(2);
        JButton peek = new JButton("Peek");
        JButton play = new JButton("Play");
        play.setVisible(false);
        peek.setVisible(false);
        lower.add(controls);

        JButton history = new JButton("History");
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Game History");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(400, 400);
                ScrollPane scroll = new ScrollPane();
                boolean alternate = false;
                JPanel p = new JPanel();
                p.setLayout(new GridLayout(g.moveHistory().length,1));
                for(String s : g.moveHistory()){
                    JLabel label = new JLabel(s);
                    label.setPreferredSize(new Dimension(200,20));
                    label.setBackground(alternate ? Color.LIGHT_GRAY : Color.WHITE);
                    p.add(label);
                    alternate = !alternate;
                }
                if(g.moveHistory().length == 0){
                    JLabel label = new JLabel("No moves made yet!");
                    scroll.add(label);
                }else{
                    scroll.add(p);
                }
                frame.getContentPane().add(scroll, BorderLayout.CENTER);

                //Display the window.
                frame.setVisible(true);
            }
        });
        controls.add(history);


        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                oriX.setText("");
                oriY.setText("");
                desX.setText("");
                desY.setText("");
                statusLabel.setText("--Welcome to Chess--");
                turn.setText("Turn: WHITE");
                g.reset();
                canvas.repaint();
            }
        });
        controls.add(reset);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final JOptionPane optionPane = new JOptionPane(
                            "Attempt move: " + g.b.get(Integer.parseInt(oriX.getText()), Integer.parseInt(oriY.getText())).getSymbol()+"@" +oriX.getText() + " " + oriY.getText() + " to " + desX.getText() + " " + desY.getText(),
                            JOptionPane.QUESTION_MESSAGE,
                            JOptionPane.YES_NO_OPTION);
                    optionPane.createDialog("Move?").setVisible(true);
                    if(optionPane.getValue().equals(JOptionPane.NO_OPTION)){
                        statusLabel.setText("--Move Undone--");
                        oriX.setText("");
                        oriY.setText("");
                        desX.setText("");
                        desY.setText("");
                        canvas.nullifyActivePiece();
//                        peek.doClick();
//                        play.doClick();
                    }else{
                        boolean success = g.move(Integer.parseInt(oriX.getText()), Integer.parseInt(oriY.getText()), Integer.parseInt(desX.getText()), Integer.parseInt(desY.getText()));
                        if (success) {
                            System.out.println("g.move(" + Integer.parseInt(oriX.getText()) + ", " + Integer.parseInt(oriY.getText()) + ", " +Integer.parseInt(desX.getText()) + ", " + Integer.parseInt(desY.getText()) + ")");
                            StringBuilder status = new StringBuilder();
                            status.append(oriX.getText() + " " + oriY.getText() + " to " + desX.getText() + " " + desY.getText());

                            if (g.moveHistory.peek().contains("check")) {
                                status.append(g.moveHistory.peek());
                                final JDialog dialog = new JDialog();
                                dialog.setTitle("Check");
                                dialog.setSize(200,100);
                                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                JLabel label = new JLabel();
                                dialog.add(label);
                                dialog.setVisible(true);
                            }


//                            for (Side s : Side.values()) {
//                                if (g.isInCheck(s)) {
//                                    status.append(s.toString()).append(" is in check. ");
//                                }
////                                if (g.isCheckMate(s)) {
////                                    status.append(s).append(" is in checkmate");
////                                }
//                            }
                            statusLabel.setText(status.toString());
                            oriX.setText("");
                            oriY.setText("");
                            desX.setText("");
                            desY.setText("");

                        } else {
                            statusLabel.setText("Invalid move");
                        }
                    }

                }catch (Exception ex){
                    statusLabel.setText("Invalid move " + ex.getMessage());
                }
                for(Side s : Side.values()){

//                    if(g.isCheckMate(s)){
//
//                        final JOptionPane optionPane = new JOptionPane(
//                                s.toString() + " is in checkmate!\n Show history?",                                JOptionPane.QUESTION_MESSAGE,
//                                JOptionPane.YES_NO_OPTION);
//                        optionPane.createDialog("Checkmate").setVisible(true);
//                        if(optionPane.getValue().equals(JOptionPane.YES_OPTION)) {
//                            history.doClick();
//                        }else{
//                            g.reset();
//                        }
//
//                    }else
//                    System.out.println("test");
                    if(g.isInCheck(s)){
//                        final JDialog dialog = new JDialog();
//                        dialog.setTitle("Check");
//                        dialog.setSize(200,100);
//                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//                        JLabel label = new JLabel();
//                        dialog.add(label);
//                        dialog.setVisible(true);
                    }
                }

                turn.setText("Turn: " + g.getCurrentTurn().toString());
                canvas.repaint();
            }
        });
        controls.add(play);
        canvas = new ChessEngine(g);
        canvas.setSize(640, 640);
        frame.setSize(640,700);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(lower, BorderLayout.SOUTH);
        canvas.addMouseListener(new MouseListener() {
            public static final int num_squares = 8;
            @Override
            public void mouseClicked(MouseEvent e) {
//                statusLabel.setText("mousePressed"+e.getX()+","+e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = (int) (((double) e.getX() / (double) canvas.getWidth() )* (double) num_squares);
                int y = (int) (((double) e.getY() / (double) canvas.getHeight() )* (double) num_squares);
                try {
                    if (oriX.getText().isEmpty()) {
//                        if(g.b.get(x,y) != null && g.b.get(x,y).getSide() != g.currentTurn){
//                            canvas.nullifyActivePiece();
//                            oriX.setText("");
//                            oriY.setText("");
//                            desX.setText("");
//                            desY.setText("");
//                        } else {
                            if (g.b.get(x, y) != null && g.b.get(x, y).getSide() == g.currentTurn) {
                                oriX.setText(Integer.toString(x));
                                oriY.setText(Integer.toString(y));
                                peek.doClick();
                            } else {
                                canvas.nullifyActivePiece();
                                oriX.setText("");
                                oriY.setText("");
                                desX.setText("");
                                desY.setText("");
                            }
//                        }
                    } else if(desX.getText().isEmpty() || desY.getText().isEmpty() || (g.canMove(Integer.parseInt(oriX.getText()), Integer.parseInt(oriY.getText()), x, y, g.currentTurn) && Integer.parseInt(desX.getText()) != x && Integer.parseInt(desY.getText()) != y)) {
//                        if (Integer.parseInt(oriX.getText()) != x || Integer.parseInt(oriY.getText()) != y) {
//                            if((g.b.get(x,y) != null && g.b.get(x,y).getSide() == g.currentTurn && canvas.getActivePiece() != null)) {
//                                canvas.nullifyActivePiece();
//                                oriX.setText("");
//                                oriY.setText("");
//                                desX.setText("");
//                                desY.setText("");
//                            } else {
                                try {
                                    if (g.canMove(Integer.parseInt(oriX.getText()), Integer.parseInt(oriY.getText()), x, y, g.currentTurn)) {
                                        desX.setText(Integer.toString(x));
                                        desY.setText(Integer.toString(y));
                                        play.doClick();
                                        canvas.nullifyActivePiece();
                                        oriX.setText("");
                                        oriY.setText("");
                                        desX.setText("");
                                        desY.setText("");
                                    } else {
                                        if (g.b.get(x, y) != null && g.b.get(x, y).getSide() == g.currentTurn) {
                                            oriX.setText(Integer.toString(x));
                                            oriY.setText(Integer.toString(y));
                                            peek.doClick();
                                        } else {
                                            canvas.nullifyActivePiece();
                                            oriX.setText("");
                                            oriY.setText("");
                                            desX.setText("");
                                            desY.setText("");
                                        }
                                    }
                                } catch (Exception ignored) {
                                }
//                            }
                    }else{
//                        play.doClick();
//                        oriX.setText("");
//                        oriY.setText("");
//                        desX.setText("");
//                        desY.setText("");
                    }
                }catch (Exception ex){
                    statusLabel.setText("Invalid move " + ex.getMessage());
                }
                canvas.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        peek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setActivePiece(Integer.parseInt(oriX.getText()), Integer.parseInt(oriY.getText()));
                canvas.repaint();
            }
        });
        controls.add(peek);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }


}

class ChessEngine extends Canvas {

    private final Game game;
    private final Board board;
    private Piece activePiece = null;

    public void setActivePiece(int x, int y){
        activePiece = board.get(x,y);
    }

    public void nullifyActivePiece() {
        activePiece = null;
    }

    public Piece getActivePiece() {
        return activePiece;
    }

    public ChessEngine(Game g) {
        this.game = g;
        this.board = g.b;
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {

        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        int pxPerHor = getWidth() / 8;
        for (int i = 0; i < 8; i++) {
            int lenAtIdx = 8;
            int pxPerVer = getHeight() / lenAtIdx;
            for (int j = 0; j < 8; j++) {
                Color c = i  % 2 == j % 2 ? Color.LIGHT_GRAY : Color.DARK_GRAY;
                g.setColor(c);
                g.fillRect(i * pxPerHor, j * pxPerVer,pxPerHor, pxPerVer);
                if(activePiece != null){
                    if(game.canMove(activePiece.x, activePiece.y, i,j,game.currentTurn)){
                        c = i  % 2 == j % 2 ? Color.GREEN.darker() : Color.GREEN.darker().darker();
                        g.setColor(c);
                        g.fillRect(i * pxPerHor, j * pxPerVer,pxPerHor, pxPerVer);
                    }else if (activePiece.x==i && activePiece.y==j){
                        g.setColor(Color.BLUE.brighter());
                        g.fillRect(i * pxPerHor, j * pxPerVer,pxPerHor, pxPerVer);
                    }
                }
                Color d = i  % 2 == j % 2 ? Color.BLACK : Color.WHITE;
                g.setColor(d);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
                g.drawString(i +", "+ j,i * pxPerHor, j * pxPerVer + pxPerVer);

                if(board.get(i,j) != null){
                    d = board.get(i,j).getSide() == Side.WHITE? Color.WHITE : Color.BLACK;
                    g.setColor(d);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, Math.min(pxPerHor, pxPerVer)));
                    g.drawString(board.get(i,j).getSymbol(),i * pxPerHor, j * pxPerVer + pxPerVer - 8);
                }


            }
        }

    }
}
