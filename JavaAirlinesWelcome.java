import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class JavaAirlinesWelcome {

    public static void main(String[] args) {
        JFrame welcomeFrame = new JFrame("Air Alliance");
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        welcomeFrame.setSize(400, 300);
        setBackgroundImage(welcomeFrame, "C:\\Users\\kumat\\Downloads\\ai.jpg");

        JLabel welcomeLabel = new JLabel("Hello, Welcome to Air Alliance!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        makeComponentTransparent(welcomeLabel);

        JButton startButton = new JButton("Let's Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeFrame.dispose(); 
                new LoginWindow(); 
            }
        });
        makeComponentTransparent(startButton);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(welcomeLabel, BorderLayout.CENTER);
        contentPane.add(startButton, BorderLayout.PAGE_END);
        makeComponentTransparent(contentPane);

        welcomeFrame.setContentPane(contentPane);
        welcomeFrame.setVisible(true);
    }

    private static void setBackgroundImage(JFrame frame, String imagePath) {
        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            ImagePanel backgroundPanel = new ImagePanel(img);
            frame.setContentPane(backgroundPanel);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    private static void makeComponentTransparent(Component component) {
        if (component instanceof JComponent) {
            ((JComponent) component).setOpaque(false);
        }
    }

    static class ImagePanel extends JPanel {
        private final Image img;

        public ImagePanel(Image img) {
            this.img = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this);
        }
    }
}