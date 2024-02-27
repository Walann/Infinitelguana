import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfiniteIguana extends JPanel implements ActionListener, KeyListener {
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private JButton resetButton;

    public InfiniteIguana() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(this);
        setBackground(Color.WHITE);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        initGame();

        resetButton = new JButton("Reset");
        resetButton.setBounds(B_WIDTH - 100, B_HEIGHT + 40, 80, 30);
        resetButton.addActionListener(e -> resetGame());
        this.add(resetButton);
        resetButton.setFocusable(false);
    }

    private void initGame() {
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            g.setColor(Color.GREEN);
            g.fillRect(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
                } else {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.BLACK);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private void resetGame() {
        inGame = true;
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
        initGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_UP) && (!downDirection)) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }

        if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fancy Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new InfiniteIguana());
            frame.pack();
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}
