package hugocorp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.*;


public class main {

    private JFrame jframe;
    private JPanel jpanel;
    private JButton jbutton;
    private JLabel jlabel;
    private JTextField jtextfield1;
    private JTextField jtextfield2;

    public static ArrayList<String> allowed_extension = new ArrayList<String>();
    public static File[] files;
    public main(){ gui(); }
    public void gui(){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jframe = new JFrame("Re_Sizer");
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLocation(dim.width/2-jframe.getSize().width/2, dim.height/2-jframe.getSize().height/2);

        jpanel = new JPanel();
        jpanel.setBackground(Color.DARK_GRAY);
        jpanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        jbutton = new JButton("Resize");
        jbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(files[0].getAbsolutePath().substring(0, files[0].getAbsolutePath().lastIndexOf('\\') + 1) + "\\resized");
                try{
                    if(f.mkdir()) {
                        System.out.println("Directory Created");
                    } else {
                        System.out.println("Directory is not created");
                    }
                } catch(Exception g){
                    g.printStackTrace();
                }

                if (!jtextfield1.getText().equals("Wysokość...") && !jtextfield2.getText().equals("Szerokość...")) {
                    for (File i : files) {
                        if (!allowed_extension.contains(i.getAbsolutePath().substring(i.getAbsolutePath().lastIndexOf(".") + 1).trim())){ continue; }
                        String input = i.getAbsolutePath();
                        String output = input.substring(0, input.lastIndexOf('\\') + 1) + "resized\\" + input.substring(input.lastIndexOf('\\') + 1);
                        try {
                            resize(input, output, Integer.parseInt(jtextfield2.getText()), Integer.parseInt(jtextfield1.getText()));
                        } catch (IOException ex) {
                            System.out.println("Error resizing the image.");
                            ex.printStackTrace();
                        }
                    }
                }else if (jtextfield1.getText().equals("Wysokość...") && jtextfield2.getText().equals("Szerokość...")){
                    System.out.println("Nie podano wartości");
                }else if (!jtextfield1.getText().equals("Wysokość...")){
                    for (File i : files) {
                        if (!allowed_extension.contains(i.getAbsolutePath().substring(i.getAbsolutePath().lastIndexOf(".") + 1).trim())){ continue; }
                        String input = i.getAbsolutePath();
                        String output = input.substring(0, input.lastIndexOf('\\') + 1) + "resized\\" + input.substring(input.lastIndexOf('\\') + 1);
                        try {
                            resize(input, output, Integer.parseInt(jtextfield1.getText()), "W");
                        } catch (IOException ex) {
                            System.out.println("Error resizing the image.");
                            ex.printStackTrace();
                        }
                    }

                }else if (jtextfield1.getText().equals("Wysokość...")){
                    for (File i : files) {
                        if (!allowed_extension.contains(i.getAbsolutePath().substring(i.getAbsolutePath().lastIndexOf(".") + 1).trim())){ continue; }
                        String input = i.getAbsolutePath();
                        String output = input.substring(0, input.lastIndexOf('\\') + 1) + "resized\\" + input.substring(input.lastIndexOf('\\') + 1);
                        try {
                            resize(input, output, Integer.parseInt(jtextfield2.getText()), "S");
                        } catch (IOException ex) {
                            System.out.println("Error resizing the image.");
                            ex.printStackTrace();
                        }
                    }

                }
            }
        });

        jlabel = new JLabel("Podaj wymiary: (px, px)");
        jlabel.setForeground(Color.WHITE);

        jtextfield1 = new JTextField("Wysokość...");
        jtextfield1.setMaximumSize(new Dimension(150, 50));
        jtextfield1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jtextfield1.getText().equals("Wysokość...")) {
                    jtextfield1.setText("");
                    jtextfield1.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jtextfield1.getText().isEmpty()) {
                    jtextfield1.setForeground(Color.GRAY);
                    jtextfield1.setText("Wysokość...");
                }

            }
        });
        jtextfield2 = new JTextField("Szerokość...");
        jtextfield2.setMaximumSize(new Dimension(150, 50));
        jtextfield2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jtextfield2.getText().equals("Szerokość...")) {
                    jtextfield2.setText("");
                    jtextfield2.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jtextfield2.getText().isEmpty()) {
                    jtextfield2.setForeground(Color.GRAY);
                    jtextfield2.setText("Szerokość...");
                }

            }
        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        jpanel.add(jlabel, c);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        jpanel.add(jtextfield1, c);
        c.gridx = 1;
        c.gridy = 1;
        jpanel.add(jtextfield2, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        jpanel.add(jbutton, c);

        jframe.add(jpanel);
        jframe.pack();
        jframe.setSize(300, 150);
    }

    public static void resize(String inputImagePath,
                              String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    public static void resize(String inputImagePath,
                              String outputImagePath, int lenght, String type)
            throws IOException {
        if (type.equals("W")){

            // reads input image
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);
            float ratio = (float)inputImage.getHeight()/(float)inputImage.getWidth();
            int width = (int)(lenght/ratio);

            // creates output image
            BufferedImage outputImage = new BufferedImage(width,
                    lenght, inputImage.getType());

            // scales the input image to the output image
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, width, lenght, null);
            g2d.dispose();

            // extracts extension of output file
            String formatName = outputImagePath.substring(outputImagePath
                    .lastIndexOf(".") + 1);

            // writes to output file
            ImageIO.write(outputImage, formatName, new File(outputImagePath));

        }else if (type.equals("S")){

            // reads input image
            File inputFile = new File(inputImagePath);
            BufferedImage inputImage = ImageIO.read(inputFile);
            float ratio = (float)inputImage.getHeight()/(float)inputImage.getWidth();
            int height = (int)(ratio*lenght);

            // creates output image
            BufferedImage outputImage = new BufferedImage(lenght,
                    height, inputImage.getType());

            // scales the input image to the output image
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, lenght, height, null);
            g2d.dispose();

            // extracts extension of output file
            String formatName = outputImagePath.substring(outputImagePath
                    .lastIndexOf(".") + 1);

            // writes to output file
            ImageIO.write(outputImage, formatName, new File(outputImagePath));

        }
    }

    public static void main(String[] args) {
        allowed_extension.add("jpg");
        allowed_extension.add("png");
        allowed_extension.add("bmp");
        allowed_extension.add("tiff");
        allowed_extension.add("gif");

        String path = "";
        for(String i:args){ path += " " + i; }
        path = path.substring(1, path.length());

        files = new File(path).listFiles();
        new main();
    }

}