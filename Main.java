import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    private static final String jFrameTitle = "Synthesizer V2.0 | CyberBellum";
    private static final JFrame jFrame = new JFrame(jFrameTitle);
    private static final JPanel jPanel = new JPanel();
    private static final JLabel jLabel = new JLabel();

    private static KeyListener keyListener;
    private static final String jLabelStr = "Synthesizer V2.0 | CyberBellum";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 90;

    private final static AudioFormat audioFormat = new AudioFormat((float) 44100, 8, 1, false, false);//44100
    private static SourceDataLine sourceDataLine;
    static { try { sourceDataLine = AudioSystem.getSourceDataLine(audioFormat); } catch (LineUnavailableException e) { e.printStackTrace(); } }

    private static int COUNTER = 0;                 // Always 0
    private static float RATE = 44100;              // 44100
    private static float HERTZ = 0;                   // 400
    private static double WAVETYPE = 2;             // Sine
    private static byte[] SDLBUFFER = new byte[1];  // sourceDataLineBuffer

    public static void main(String[] args)
    {
        Thread synthesizerThread = new Thread(Main::synthesizer); synthesizerThread.start(); synthesizerThread.setPriority(Thread.MAX_PRIORITY);
        keyListener = new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e)
            {
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_Z: //C4
                            setSynthesizerValues(44100,261.63f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|C4|");
                            break;
                        case KeyEvent.VK_X: //D4
                            setSynthesizerValues(44100,293.66f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|D4|");
                            break;
                        case KeyEvent.VK_C: //E4
                            setSynthesizerValues(44100,329.63f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|E4|");
                            break;
                        case KeyEvent.VK_V: //F4
                            setSynthesizerValues(44100,349.23f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|F4|");
                            break;
                        case KeyEvent.VK_B: //G4
                            setSynthesizerValues(44100,392.00f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|G4|");
                            break;
                        case KeyEvent.VK_N: //A4
                            setSynthesizerValues(44100,440.00f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|A4|");
                            break;
                        case KeyEvent.VK_M: //B4
                            setSynthesizerValues(44100,493.88f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|B4|");
                            break;
                        case KeyEvent.VK_COMMA: //C5
                            setSynthesizerValues(44100,523.25f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|C5|");
                            break;
                        case KeyEvent.VK_PERIOD: //D5
                            setSynthesizerValues(44100,587.33f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|D5|");
                            break;
                        case KeyEvent.VK_SLASH: //E5
                            setSynthesizerValues(44100,659.25f,2);
                            jLabel.setForeground(Color.GREEN);jLabel.setText("|E5|");
                            break;
                    }
                } catch (LineUnavailableException e1) { e1.printStackTrace(); }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_C
                        || e.getKeyCode() == KeyEvent.VK_V || e.getKeyCode() == KeyEvent.VK_B || e.getKeyCode() == KeyEvent.VK_N
                        || e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_COMMA || e.getKeyCode() == KeyEvent.VK_PERIOD
                        || e.getKeyCode() == KeyEvent.VK_SLASH ) {
                    jLabel.setText(" ");
                    COUNTER = 0;
                    RATE = 0;
                    HERTZ = 0;
                    WAVETYPE = 0;
                    SDLBUFFER[0] = 0;
                    sourceDataLine.stop();
                    sourceDataLine.close();
                }
            }
        };
        createFrame();
    }

    private static void synthesizer()
    {
        while (COUNTER < Integer.MAX_VALUE) {
            COUNTER++;
            double angle = COUNTER / (RATE / HERTZ) * WAVETYPE * Math.PI;
            //double angle = Math.random(); NOISE
            //double angle = COUNTER / (RATE / HERTS) * WAVETYPE * 2;
            //double angle = COUNTER / (RATE / HERTZ) * WAVETYPE * Math.PI;
            SDLBUFFER[0] = (byte) ((Math.sin(angle) *100));
            sourceDataLine.write(SDLBUFFER, 0, 1);
        }
        sourceDataLine.drain();
        sourceDataLine.stop();
    }

    public static void setSynthesizerValues(int rate,float hz, int wave) throws LineUnavailableException
    {
        RATE = rate;
        HERTZ = hz;
        WAVETYPE = wave;
        sourceDataLine.open();
        sourceDataLine.start();
    }

    private static void createFrame()
    {
        jFrame.setTitle(jFrameTitle);
        jFrame.setSize(WIDTH,HEIGHT);
        jFrame.addKeyListener(keyListener);
        jFrame.add(jPanel);
        jPanel.setBackground(Color.black);
        jPanel.add(jLabel);
        jLabel.setText(jLabelStr);
        jLabel.setForeground(Color.white);
        jLabel.setFont(new Font(jLabel.getName(), Font.PLAIN, 30));
        jFrame.setAlwaysOnTop(true);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
