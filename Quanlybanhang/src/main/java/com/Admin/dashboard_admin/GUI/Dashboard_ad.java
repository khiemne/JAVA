package com.Admin.dashboard_admin.GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.Box;
import java.util.HashSet;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.Set;
import com.ComponentandDatabase.Components.MyButton;
import com.ComponentandDatabase.Components.MyPanel;
import com.Admin.dashboard_admin.BUS.BusProfile_ad;
import com.Admin.login.GUI.Login;
import com.Admin.home.GUI.Form_Home;
import com.Admin.category.GUI.Form_Category;
import com.Admin.order.GUI.Form_Order;
import com.Admin.customer.GUI.Form_Customer;
import com.Admin.product.GUI.Form_Product;
import com.Admin.export.GUI.Form_Export;
import com.Admin.inventory.GUI.Form_Inventory;
import com.Admin.statistics.GUI.Form_Statistics;
import com.Admin.promotion.GUI.Form_Promotion;
 

public class Dashboard_ad extends JFrame {
    private JPanel bg, contentPanel;
    private JLabel title, lblDateTime, title_exit;
    private MyButton logout;
    private MyPanel panelTitle;
    private Menu menu;
    private CardLayout cardLayout;
    
    private boolean isFullScreen = true;
    private JLabel selectedLabel = null;
    private final Color hoverColor = new Color(173, 216, 230, 200);
    private final Color transparentColor = new Color(0, 0, 0, 0);
    public static String adminID, adminName;
    
    public Dashboard_ad(String adminID) {
        Dashboard_ad.adminID = adminID;
        
        // Cập nhật SessionManager với Admin_ID hiện tại
        com.ComponentandDatabase.Session.SessionManager sessionManager = com.ComponentandDatabase.Session.SessionManager.getInstance();
        sessionManager.setCurrentAdmin(adminID, adminName);
        
        initComponents();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullScreen();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    exitFullScreen();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }
  private void initComponents() {
    // --- PANEL NỀN CHÍNH ---
    bg = new JPanel(new BorderLayout());

    // --- PANEL TIÊU ĐỀ ---
    panelTitle = new MyPanel(new BorderLayout());
    panelTitle.setPreferredSize(new Dimension(0, 50));
    panelTitle.setGradientColors(Color.decode("#1CB5E0"), Color.decode("#4682B4"), MyPanel.VERTICAL_GRADIENT);

    // --- Icon và text cho tiêu đề trung tâm ---
    ImageIcon titleIcon = loadIconResource("/Icons/Admin_icon/Title_icon.png", 40, 40);
    title = new JLabel("Electric Bicycle Sales", titleIcon, JLabel.CENTER);
    title.setFont(new Font("Times New Roman", Font.BOLD, 20));
    title.setForeground(Color.WHITE);

    // --- Icon và text cho nút exit ở bên trái ---
    ImageIcon exit_icon = loadIconResource("/Icons/Admin_icon/exit_full_screen.png", 20, 20);
    title_exit = new JLabel("", exit_icon, JLabel.LEFT);
    title_exit.setFont(new Font("Times New Roman", Font.BOLD, 18));
    title_exit.setForeground(Color.WHITE);
     addHoverEffectForExit(title_exit);

    // --- Đặt title_exit vào panel trái ---
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10)); // căn trái, padding 20px ngang, 10px dọc
    leftPanel.setOpaque(false); // cho trong suốt
    leftPanel.add(title_exit);

    // --- Panel bên phải chứa thời gian và logout ---
    logout = new MyButton("Logout", 20);
    logout.setBackgroundColor(Color.decode("#E55454"));
    logout.setPressedColor(Color.decode("#C04444"));
    logout.setHoverColor(Color.decode("#FF7F7F"));
    logout.setFont(new Font("Times New Roman", Font.BOLD, 18));
    logout.setForeground(Color.WHITE);
    logout.setButtonIcon("src\\main\\resources\\Icons\\Admin_icon\\logout.png", 25, 25, 10, SwingConstants.RIGHT, SwingConstants.CENTER);
    logout.addActionListener((e) -> {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
        
    });

    lblDateTime = new JLabel();
    lblDateTime.setFont(new Font("Times New Roman", Font.BOLD, 18));
    lblDateTime.setForeground(Color.WHITE);
    updateDateTime();

    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
    rightPanel.setOpaque(false);
    rightPanel.add(Box.createHorizontalGlue());
    rightPanel.add(lblDateTime);
    rightPanel.add(Box.createHorizontalStrut(100));
    rightPanel.add(logout);

    JPanel containerPanel = new JPanel(new BorderLayout());
    containerPanel.setOpaque(false);
    containerPanel.add(rightPanel, BorderLayout.EAST);

    // --- Gộp mọi thứ vào panelTitle ---
    panelTitle.add(leftPanel, BorderLayout.WEST);
    panelTitle.add(title, BorderLayout.CENTER);
    panelTitle.add(containerPanel, BorderLayout.EAST);
    bg.add(panelTitle, BorderLayout.NORTH);

    // --- MENU & CONTENT ---
    menu = new Menu(this);
    menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
    menu.setPreferredSize(new Dimension(260, 0));

    cardLayout = new CardLayout();
    contentPanel = new JPanel(cardLayout);

    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.add(menu, BorderLayout.WEST);
    wrapper.add(contentPanel, BorderLayout.CENTER);

    bg.add(wrapper, BorderLayout.CENTER);

    // --- THÊM FORM ---
    contentPanel.add(new Form_Home(this), "Home");
    contentPanel.add(new Form_Category(), "Category");
    contentPanel.add(new Form_Order(), "Order");
    contentPanel.add(new Form_Customer(), "Customer");
    contentPanel.add(new Form_Product(), "Product");
    contentPanel.add(new Form_Inventory(), "Inventory");
    contentPanel.add(new Form_Export(), "Export");
    contentPanel.add(new Form_Promotion(), "Promotion");
    contentPanel.add(new Form_Statistics(), "Statistics");
    contentPanel.revalidate();
    contentPanel.repaint();


    // --- FRAME SETTINGS ---
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Dashboard Admin");
    setFullScreen(); // Full màn hình
    setContentPane(bg);

    // --- CẬP NHẬT GIỜ ---
    Timer timer = new Timer(1000, e -> updateDateTime());
    timer.start();
}

    public static String getAdminName(String adminID) {
        try {
            if (adminID == null || adminID.trim().isEmpty()) {
                return "Unknown Admin";
            }

            BusProfile_ad busProfile = new BusProfile_ad();
            String adminName = busProfile.getAdminName(adminID);

            return adminName != null ? adminName : "Unknown Admin";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error getting name";
        }
    }

    private void updateLayout() {
        int w = getWidth() - 260; // 260 là chiều rộng menu
        int h = getHeight() - 50; // 50 là chiều cao panelTitle
        contentPanel.setBounds(0, 0, w, h);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setFullScreen() {
        dispose();
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        isFullScreen = true;
    }

    private void exitFullScreen() {
        if (isFullScreen) {
            toggleFullScreen();
        }
    }


    private void updateDateTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("'Date:' dd/MM/yyyy  '-' HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        lblDateTime.setText(currentTime);
    }

   public void showForm(String formName) {
    SwingUtilities.invokeLater(() -> {
        cardLayout.show(contentPanel, formName);
        contentPanel.revalidate();
        contentPanel.repaint();
    });
}

    
    // Hàm áp hiệu ứng hover và click + xử lý toàn màn hình
    private void addHoverEffectForExit(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (label != selectedLabel) {
                    setLabelHoverStyle(label);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (label != selectedLabel) {
                    resetLabelStyle(label);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Cập nhật trạng thái chọn
                if (selectedLabel != null && selectedLabel != label) {
                    resetLabelStyle(selectedLabel);
                }
                selectedLabel = label;
                setLabelHoverStyle(label); // Áp dụng hiệu ứng khi chọn

                // Chuyển đổi giữa fullscreen và exit fullscreen
                toggleFullScreen();
            }
        });
    }

    // Hàm xử lý bật/tắt toàn màn hình
    private void toggleFullScreen() {
        dispose(); // Đóng JFrame tạm thời để thay đổi chế độ

        if (isFullScreen) {
            setUndecorated(false);
            setExtendedState(JFrame.NORMAL);
        } else {
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        setVisible(true);
        isFullScreen = !isFullScreen; // Đảo trạng thái giữa fullscreen và exit fullscreen

    }

    // Style khi hover (áp dụng cả khi được chọn)
    private void setLabelHoverStyle(JLabel label) {
        label.setOpaque(true);
        label.setBackground(hoverColor);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.revalidate();
        label.repaint();
    }

    // Reset style khi không chọn
    private void resetLabelStyle(JLabel label) {
        label.setOpaque(false);
        label.setBackground(transparentColor);
        label.revalidate();
        label.repaint();
    }

    
    private ImageIcon loadIconResource(String resourcePath, int width, int height) {
        java.net.URL resource = getClass().getResource(resourcePath);
        if (resource == null) return null;
        ImageIcon icon = new ImageIcon(resource);
        Image img = icon.getImage();
        Image resized = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

      public static void main(String args[]) {
         try {
              UIManager.setLookAndFeel(new FlatLightLaf());
          } catch (Exception e) {
              e.printStackTrace();
          }
     
          SwingUtilities.invokeLater(() -> new Dashboard_ad(adminID).setVisible(true));
      }
      
      
  }