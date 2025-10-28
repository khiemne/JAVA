package com.User.dashboard_user.GUI;

import java.awt.*;
import java.awt.RenderingHints;
import javax.swing.*;
import com.ComponentandDatabase.Components.MyButton;
import com.ComponentandDatabase.Components.MyPanel;
import com.User.home.GUI.Home_user;
import com.User.Cart.GUI.Form_Cart;
import com.User.order.GUI.Order_Form;
import com.User.dashboard_user.BUS.BUSProfile_cus;
import com.User.login_user.GUI.PanelLoginandRegister_User;

public class Dashboard_user extends JFrame {
    private JPanel bg, contentPanel;
    private JLabel title, lblDateTime;
    private MyButton logout;
    private MyPanel panelTitle;
    private Menu_user menu;
    private CardLayout cardLayout;
    private BUSProfile_cus busProfile;
    public static String email, customerID;
    private String userName;

    public Dashboard_user() {
        this(null); // Gọi constructor với email = null
    }
    
    public Dashboard_user(String userEmail) {
        initComponents(userEmail);
        loadUserInfo(); // Load thông tin user
        setSize(1400, 900); // Kích thước tối ưu cho giao diện hiện đại
        setLocationRelativeTo(null); // Hiển thị giữa màn hình
        setResizable(true);
        setVisible(true);
    }

    
    private void initComponents(String userEmail) {
        // PANEL NỀN CHÍNH
        bg = new JPanel(new BorderLayout());

        // PANEL TIÊU ĐỀ - Thiết kế hiện đại với gradient và shadow
        panelTitle = new MyPanel(new BorderLayout());
        panelTitle.setPreferredSize(new Dimension(0, 140)); // Tăng height để có đủ không gian
        panelTitle.setGradientColors(Color.decode("#667eea"), Color.decode("#764ba2"), MyPanel.VERTICAL_GRADIENT);
        panelTitle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 4, 0, Color.decode("#5a67d8")),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Tiêu đề chính với thiết kế hiện đại
        ImageIcon titleIcon = new ImageIcon(getClass().getResource("/Icons/Admin_icon/Title_icon.png"));
        Image img = titleIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Giảm size icon
        titleIcon = new ImageIcon(img);
        
        // Tạo panel tiêu đề với layout hiện đại - căn giữa hoàn hảo
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Tăng padding để có đủ không gian
        
        // Tiêu đề chính với typography hiện đại và sinh động - căn giữa
        title = new JLabel("NPK STORE", titleIcon, JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30)); // Giảm size để đảm bảo không bị cắt
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Bỏ tất cả padding
        
        // Thêm hiệu ứng text shadow cho title
        title.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Vẽ shadow
                g2.setColor(new Color(0, 0, 0, 120));
                g2.drawString(s, textX + 3, textY + 3);
                
                // Vẽ text chính
                g2.setColor(l.getForeground());
                g2.drawString(s, textX, textY);
                
                g2.dispose();
            }
        });
        
        // Subtitle với style sinh động và rõ ràng - căn giữa
        JLabel subtitle = new JLabel("Electric Bicycle Sales Management System");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Giảm size để đảm bảo không bị cắt
        subtitle.setForeground(Color.WHITE); // Màu trắng rõ ràng
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Bỏ tất cả padding
        
        // Đảm bảo text hiển thị rõ ràng
        subtitle.setOpaque(false);
        subtitle.setVisible(true);
        
        // Thêm hiệu ứng text shadow để sinh động hơn
        subtitle.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Vẽ shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(s, textX + 2, textY + 2);
                
                // Vẽ text chính
                g2.setColor(l.getForeground());
                g2.drawString(s, textX, textY);
                
                g2.dispose();
            }
        });
        
        // Tạo container với GridBagLayout để kiểm soát chính xác vị trí
        JPanel textContainer = new JPanel(new GridBagLayout());
        textContainer.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0); // Không có padding
        textContainer.add(title, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 0, 0); // 8px khoảng cách giữa title và subtitle
        textContainer.add(subtitle, gbc);
        
        titleContainer.add(textContainer, BorderLayout.CENTER);

        // Panel thông tin người dùng và controls bên phải - Layout hiện đại
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        
        // Panel thông tin người dùng với design hiện đại
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        userInfoPanel.setOpaque(false);
        
        // DateTime display với design hiện đại
        lblDateTime = new JLabel();
        lblDateTime.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDateTime.setForeground(Color.WHITE);
        lblDateTime.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#A0AEC0"), 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        lblDateTime.setBackground(Color.decode("#4A5568"));
        lblDateTime.setOpaque(true);
        updateDateTime();
        userInfoPanel.add(lblDateTime);
        
        rightPanel.add(userInfoPanel, BorderLayout.WEST);
        
        // Nút logout với design hiện đại
        logout = new MyButton("Logout", 25);
        logout.setBackgroundColor(Color.decode("#EF4444"));
        logout.setPressedColor(Color.decode("#DC2626"));
        logout.setHoverColor(Color.decode("#F87171"));
        logout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logout.setForeground(Color.WHITE);
        logout.setPreferredSize(new Dimension(140, 55));
        logout.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#FEE2E2"), 2),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        logout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                com.User.login_user.GUI.Login_User loginForm = new com.User.login_user.GUI.Login_User();
                loginForm.setVisible(true);
            }
        });
        rightPanel.add(logout, BorderLayout.EAST);

        panelTitle.add(titleContainer, BorderLayout.CENTER);
        panelTitle.add(rightPanel, BorderLayout.SOUTH);
        bg.add(panelTitle, BorderLayout.NORTH);

        // MENU + CONTENT với layout hiện đại
        menu = new Menu_user(this);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setPreferredSize(new Dimension(300, 0));
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 3, Color.decode("#A0AEC0")),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE); // Nền trắng sáng cho content
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(menu, BorderLayout.WEST);
        wrapper.add(contentPanel, BorderLayout.CENTER);

        bg.add(wrapper, BorderLayout.CENTER);
     
        // Sử dụng email được truyền vào hoặc lấy từ static method
        if (userEmail != null && !userEmail.isEmpty()) {
            email = userEmail;
            System.out.println("🔍 DEBUG - Dashboard received email: " + email);
        } else {
            // Lấy email từ static method
            String savedEmail = PanelLoginandRegister_User.getCurrentUserEmail();
            if (savedEmail != null && !savedEmail.isEmpty()) {
                email = savedEmail;
                System.out.println("🔍 DEBUG - Dashboard got email from static method: " + email);
            } else {
                // Fallback nếu không có email
                email = "default@email.com";
                System.out.println("🔍 DEBUG - Dashboard using fallback email: " + email);
            }
        }
        busProfile = new BUSProfile_cus();
        customerID = busProfile.getCustomerID(email);
        System.out.println("🔍 DEBUG - Customer ID: " + customerID);
        // THÊM FORM
        
        Form_Cart cartForm = new Form_Cart(customerID);
        Order_Form orderForm = new Order_Form(customerID);

        // Đăng ký listener trước khi thêm vào contentPanel
        cartForm.setOrderUpdateListener(orderForm);

        // Thêm các form vào contentPanel
        contentPanel.add(new Home_user(), "Home_user");
        contentPanel.add(cartForm, "Form_Cart");
        contentPanel.add(orderForm, "Order_Form");

        // FRAME SETUP
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Dashboard Customer");
        setContentPane(bg);

        // UPDATE GIỜ THỰC TẾ
        Timer timer = new Timer(1000, e -> updateDateTime());
        timer.start();
    }

    private void updateDateTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("'Date:' dd/MM/yyyy  '-' HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        lblDateTime.setText(currentTime);
    }

    public void showForm(String formName) {
        cardLayout.show(contentPanel, formName);
    }


    // Method để lấy tên user
    public String getUserName() {
        if (userName == null && customerID != null) {
            try {
                busProfile = new BUSProfile_cus();
                userName = busProfile.getCustomerName(customerID);
            } catch (Exception e) {
                userName = "User";
            }
        }
        return userName;
    }
    
    // Method để load thông tin user khi khởi tạo
    private void loadUserInfo() {
        if (customerID != null) {
            try {
                busProfile = new BUSProfile_cus();
                userName = busProfile.getCustomerName(customerID);
            } catch (Exception e) {
                userName = "User";
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard_user());
    }
}
