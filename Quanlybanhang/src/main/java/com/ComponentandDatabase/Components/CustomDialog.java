
package com.ComponentandDatabase.Components;

import com.formdev.flatlaf.FlatLightLaf;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class CustomDialog {
    private static int mouseX, mouseY;

    // 🔊 Hàm phát âm thanh không chặn giao diện
    public static void playSound(String soundFilePath) {
        new Thread(() -> {
            try {
                File soundFile = new File(soundFilePath);
                if (!soundFile.exists()) {
                    System.out.println("⚠️ File không tồn tại: " + soundFilePath);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try {
                            audioStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 🟥 Hiển thị thông báo lỗi
    public static void showError(String message) {
        playSound("src\\main\\resources\\Sound\\error.wav");
        showDialog("Error", message, UIManager.getIcon("OptionPane.errorIcon"), Color.decode("#08BAF5"));
    }

    // 🟩 Hiển thị thông báo thành công
    public static void showSuccess(String message) {
        playSound("src\\main\\resources\\Sound\\success.wav");

        // Kiểm tra icon có tồn tại không
        String iconPath = "src\\main\\resources\\Icons\\User_icon\\success.png";
        ImageIcon successIcon = new ImageIcon(iconPath);
        if (successIcon.getIconWidth() == -1) {
            System.out.println("❌ Lỗi: Không tìm thấy file ảnh ở đường dẫn: " + iconPath);
            successIcon = (ImageIcon) UIManager.getIcon("OptionPane.informationIcon");
        } else {
            Image img = successIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            successIcon = new ImageIcon(img);
        }

        showDialog("Success", message, successIcon, Color.decode("#08BAF5"));
    }

    // 📌 Hàm hiển thị hộp thoại tùy chỉnh
    private static void showDialog(String title, String message, Icon icon, Color titleColor) {
        try {
             UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            int width = 350;
            int height = 180;

            JDialog dialog = new JDialog((Frame) null, title, true);
            dialog.setUndecorated(true);
            dialog.setSize(width, height);
            dialog.setLayout(null);
            dialog.getContentPane().setBackground(Color.WHITE);
            
            // Modern shadow effect - removed setDropShadow as it's not available in standard JDialog

            // **Tiêu đề hộp thoại với gradient**
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Gradient background
                    GradientPaint gradient = new GradientPaint(0, 0, titleColor, 0, getHeight(), 
                        new Color(titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), 200));
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    
                    g2.dispose();
                }
            };
            titlePanel.setBounds(0, 0, width, 50);

            JLabel iconLabel = new JLabel(icon);
            JLabel titleLabel = new JLabel(" " + title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

            titlePanel.add(iconLabel);
            titlePanel.add(titleLabel);

            // **Cho phép kéo hộp thoại**
            titlePanel.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            });

            titlePanel.addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    dialog.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
                }
            });

            // **Nội dung thông báo**
            JTextArea messageLabel = new JTextArea(message);
            messageLabel.setWrapStyleWord(true);
            messageLabel.setLineWrap(true);
            messageLabel.setOpaque(true);
            messageLabel.setBackground(Color.WHITE);
            messageLabel.setEditable(false);
            messageLabel.setFocusable(false);
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(messageLabel);
            scrollPane.setBorder(null);
            scrollPane.setBounds(20, 70, width - 40, 50);

            // **Panel chứa button**
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(null);
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBounds(0, height - 60, width, 50);

            // **Button "OK" với modern style**
            JButton closeButton = new JButton("OK") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Gradient background
                    GradientPaint gradient = new GradientPaint(0, 0, titleColor, 0, getHeight(), 
                        new Color(titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), 180));
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    
                    // Border
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            closeButton.setFocusPainted(false);
            closeButton.setBorderPainted(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setBounds((width - 100) / 2, 10, 100, 35);
            closeButton.addActionListener(e -> dialog.dispose());
//            closeButton.repaint();
//            closeButton.revalidate();
//            closeButton.setContentAreaFilled(true);
//            closeButton.setOpaque(true);

            buttonPanel.add(closeButton);

            // **Thêm thành phần vào dialog**
            dialog.add(titlePanel);
            dialog.add(scrollPane);
            dialog.add(buttonPanel);

            // **Bo góc hộp thoại với shadow**
            dialog.setShape(new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, 25, 25));
            
            // Thêm shadow effect cho dialog
            dialog.getRootPane().putClientProperty("Window.shadow", true);

            // **Hiển thị hộp thoại**
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });
    }
    
    public static boolean showOptionPane(String title, String message, Icon icon, Color titleColor) {
      playSound("src\\main\\resources\\Sound\\error.wav");

      try {
          UIManager.setLookAndFeel(new FlatLightLaf());
      } catch (Exception e) {
          e.printStackTrace();
      }

      final JDialog dialog = new JDialog((Frame) null, title, true);
      dialog.setUndecorated(true);
      dialog.setSize(300, 160);
      dialog.setLayout(null);
      dialog.getContentPane().setBackground(Color.WHITE);

      // **Tiêu đề hộp thoại**
      JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      titlePanel.setBackground(titleColor);
      titlePanel.setBounds(0, 0, 300, 40);

      JLabel iconLabel = new JLabel(icon);
      JLabel titleLabel = new JLabel(" " + title);
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

      titlePanel.add(iconLabel);
      titlePanel.add(titleLabel);

      // **Cho phép kéo hộp thoại**
      titlePanel.addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
              mouseX = e.getX();
              mouseY = e.getY();
          }
      });

      titlePanel.addMouseMotionListener(new MouseAdapter() {
          public void mouseDragged(MouseEvent e) {
              dialog.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
          }
      });

      // **Nội dung thông báo**
      JTextArea messageLabel = new JTextArea(message);
      messageLabel.setWrapStyleWord(true);
      messageLabel.setLineWrap(true);
      messageLabel.setOpaque(true);
      messageLabel.setBackground(Color.WHITE);
      messageLabel.setEditable(false);
      messageLabel.setFocusable(false);
      messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

      JScrollPane scrollPane = new JScrollPane(messageLabel);
      scrollPane.setBorder(null);
      scrollPane.setBounds(20, 60, 260, 40);

      // **Panel chứa button**
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(null);
      buttonPanel.setBackground(Color.WHITE);
      buttonPanel.setBounds(0, 110, 300, 40);

      final boolean[] result = {false};

      // **Button "OK"**
      JButton yesButton = new JButton("Yes");
      yesButton.setBackground(titleColor);
      yesButton.setForeground(Color.WHITE);
      yesButton.setFont(new Font("Arial", Font.BOLD, 14));
      yesButton.setFocusPainted(false);
//      yesButton.setOpaque(true);
//      yesButton.setContentAreaFilled(true);
      yesButton.setBounds(50, 5, 80, 30);
      yesButton.addActionListener(e -> {
          result[0] = true;
          dialog.dispose();
      });

      // **Button "Cancel"**
      JButton noButton = new JButton("Cancel");
      noButton.setBackground(Color.GRAY);
      noButton.setForeground(Color.WHITE);
      noButton.setFont(new Font("Arial", Font.BOLD, 14));
      noButton.setFocusPainted(false);
      noButton.setOpaque(true);
      noButton.setContentAreaFilled(true);
      noButton.setBounds(170, 5, 80, 30);
      noButton.addActionListener(e -> {
          result[0] = false;
          dialog.dispose();
      });

      buttonPanel.add(yesButton);
      buttonPanel.add(noButton);

      // **Thêm thành phần vào dialog**
      dialog.add(titlePanel);
      dialog.add(scrollPane);
      dialog.add(buttonPanel);

      // **Bo góc hộp thoại**
      dialog.setShape(new java.awt.geom.RoundRectangle2D.Float(0, 0, 300, 160, 20, 20));

      // **Hiển thị hộp thoại và đợi kết quả**
      dialog.setLocationRelativeTo(null);
      dialog.setVisible(true); // Quan trọng: Đợi hộp thoại đóng lại trước khi tiếp tục

      return result[0]; // Trả về kết quả người dùng chọn
  }
    
    public static JDialog showLoadingDialog(String title, String message) {
        JDialog loadingDialog = new JDialog((Frame) null, title, true);
        loadingDialog.setUndecorated(true);
        loadingDialog.setSize(300, 160);
        loadingDialog.setLayout(null);
        loadingDialog.getContentPane().setBackground(Color.WHITE);

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setBounds(20, 60, 260, 40);

        // 🔹 Hiển thị GIF động đúng cách
        ImageIcon loadingIcon = new ImageIcon("src\\main\\resources\\Icons\\Admin_icon\\loading.gif");
        JLabel iconLabel = new JLabel(loadingIcon);
        iconLabel.setBounds(125, 100, 50, 50);

        loadingDialog.add(messageLabel);
        loadingDialog.add(iconLabel);

        loadingDialog.setLocationRelativeTo(null);
        loadingDialog.setVisible(true);

        return loadingDialog; // Không tự động đóng, sẽ đóng khi quá trình hoàn thành
    }

    // 📌 Đóng hộp thoại chờ
    public static void closeLoadingDialog(JDialog loadingDialog) {
        if (loadingDialog != null) {
            SwingUtilities.invokeLater(() -> loadingDialog.dispose());
        }
    }


}

    

