package net.coprg.coprg;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultEditorKit;

import net.coprg.coprg.GDrive.GDriveListener;

public class CoProgramming {

    JFrame frmCoprogramming;
    private CardLayout cardCoprogramming;
    private JTextField fieldAuthorizeCode;
    private JList<String> fileList;
    private DefaultListModel<String> fileListModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CoProgramming window = new CoProgramming();
                    window.frmCoprogramming.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CoProgramming() {
        initialize();
        if (GDrive.isLoggedIn()) {
            showEdit();
        } else {
            showAuthorize();
        }
    }

    public void showEdit() {
        showCard("edit");
        refreshList();
    }

    public void showAuthorize() {
        showCard("authorize");
    }

    public void showConnect() {
        showCard("connect");
    }

    private void showCard(String name) {
        cardCoprogramming.show(frmCoprogramming.getContentPane(), name);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to set native look and feel: " + e);
        }
        frmCoprogramming = new JFrame();
        frmCoprogramming.setTitle(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.frmCoprogramming.title"));
        frmCoprogramming.setMinimumSize(new Dimension(320, 300));
        frmCoprogramming.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardCoprogramming = new CardLayout(0, 0);
        frmCoprogramming.getContentPane().setLayout(cardCoprogramming);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridLayout(0, 1, 0, 0));
        frmCoprogramming.getContentPane().add(welcomePanel, "welcome");

        JLabel lblCoprogramming = new JLabel(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.lblCoprogramming.text"));
        lblCoprogramming.setFont(new Font("Dialog", Font.BOLD, 26));
        welcomePanel.add(lblCoprogramming);
        lblCoprogramming.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel authorizePanel = new JPanel();
        authorizePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        authorizePanel
            .setLayout(new BoxLayout(authorizePanel, BoxLayout.Y_AXIS));
        frmCoprogramming.getContentPane().add(authorizePanel, "authorize");

        JPanel panel_1 = new JPanel();
        panel_1.setAlignmentY(0.0f);
        authorizePanel.add(panel_1);
        panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

        JLabel lblStep1 = new JLabel(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.lblStep1.text"));
        lblStep1.setHorizontalAlignment(SwingConstants.LEFT);
        panel_1.add(lblStep1);
        lblStep1.setFont(new Font("Dialog", Font.BOLD, 20));

        Component verticalStrut_1 = Box.createVerticalStrut(20);
        authorizePanel.add(verticalStrut_1);

        JPanel panel_2 = new JPanel();
        panel_2.setAlignmentY(0.0f);
        authorizePanel.add(panel_2);
        panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        JButton btnLink = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnLink.text"));
        btnLink.setFont(new Font("Dialog", Font.BOLD, 15));
        panel_2.add(btnLink);

        JSeparator separator = new JSeparator();
        separator.setAlignmentY(0.0f);
        separator.setAlignmentX(0.0f);
        authorizePanel.add(separator);

        JPanel panel_3 = new JPanel();
        panel_3.setAlignmentY(0.0f);
        authorizePanel.add(panel_3);
        panel_3.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

        JLabel lblStep2 = new JLabel(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.lblStep2.text"));
        lblStep2.setHorizontalAlignment(SwingConstants.LEFT);
        lblStep2.setFont(new Font("Dialog", Font.BOLD, 20));
        panel_3.add(lblStep2);

        JLabel lblEnterCode = new JLabel(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.lblEnterCode.text"));
        panel_3.add(lblEnterCode);

        Component verticalStrut_2 = Box.createVerticalStrut(20);
        panel_3.add(verticalStrut_2);

        JPanel panel_4 = new JPanel();
        FlowLayout fl_panel_4 = (FlowLayout) panel_4.getLayout();
        fl_panel_4.setVgap(0);
        fl_panel_4.setHgap(0);
        panel_4.setAlignmentY(0.0f);
        authorizePanel.add(panel_4);
        panel_4.setAlignmentX(Component.LEFT_ALIGNMENT);

        fieldAuthorizeCode = new JTextField();
        panel_4.add(fieldAuthorizeCode);
        fieldAuthorizeCode.setColumns(15);
        fieldAuthorizeCode.addMouseListener(new ContextMenuHandler());

        JButton btnAuthorize = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnAuthorize.text"));
        btnAuthorize.setFont(new Font("Dialog", Font.BOLD, 10));
        panel_4.add(btnAuthorize);

        JPanel connectPanel = new JPanel();
        frmCoprogramming.getContentPane().add(connectPanel, "connect");
        connectPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JLabel lblConnecting = new JLabel(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.lblConnecting.text"));
        lblConnecting.setHorizontalAlignment(SwingConstants.CENTER);
        connectPanel.add(lblConnecting);

        JPanel editPanel = new JPanel();
        frmCoprogramming.getContentPane().add(editPanel, "edit");
        GridBagLayout gbl_editPanel = new GridBagLayout();
        gbl_editPanel.columnWidths = new int[] { 229, 71, 0 };
        gbl_editPanel.rowHeights = new int[] { 300, 0 };
        gbl_editPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_editPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        editPanel.setLayout(gbl_editPanel);

        fileListModel = new DefaultListModel<String>();
        fileList = new JList<String>(fileListModel);
        fileList.setFont(new Font("Dialog", Font.PLAIN, 10));
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GridBagConstraints gbc_fileList = new GridBagConstraints();
        gbc_fileList.fill = GridBagConstraints.BOTH;
        gbc_fileList.insets = new Insets(0, 0, 0, 5);
        gbc_fileList.gridx = 0;
        gbc_fileList.gridy = 0;
        editPanel.add(fileList, gbc_fileList);

        JPanel toolPanel = new JPanel();
        GridBagConstraints gbc_toolPanel = new GridBagConstraints();
        gbc_toolPanel.fill = GridBagConstraints.BOTH;
        gbc_toolPanel.gridx = 1;
        gbc_toolPanel.gridy = 0;
        editPanel.add(toolPanel, gbc_toolPanel);
        toolPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JButton btnNew = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnNew.text"));
        btnNew.setFont(new Font("Dialog", Font.PLAIN, 11));
        btnNew.setMargin(new Insets(1, 1, 1, 1));
        toolPanel.add(btnNew);

        JButton btnEdit = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnEdit.text"));
        btnEdit.setFont(new Font("Dialog", Font.PLAIN, 11));
        btnEdit.setMargin(new Insets(1, 1, 1, 1));
        toolPanel.add(btnEdit);

        JButton btnExecute = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnExecute.text"));
        btnExecute.setFont(new Font("Dialog", Font.PLAIN, 11));
        btnExecute.setMargin(new Insets(1, 1, 1, 1));
        toolPanel.add(btnExecute);

        JButton btnRefresh = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnRefresh.text"));
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 11));
        btnRefresh.setMargin(new Insets(1, 1, 1, 1));
        toolPanel.add(btnRefresh);

        JButton btnLogout = new JButton(ResourceBundle.getBundle(
                    "net.coprg.coprg.messages").getString(
                        "CoProgramming.btnLogout.text"));
        btnLogout.setFont(new Font("Dialog", Font.PLAIN, 11));
        btnLogout.setMargin(new Insets(1, 1, 1, 1));
        toolPanel.add(btnLogout);

        btnAuthorize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent _) {
                showConnect();
                GDrive.authorize(fieldAuthorizeCode.getText(),
                    new GDriveListener() {
                        @Override
                        public void onSuccess() {
                            showEdit();
                        }

                        @Override
                        public void onFailure() {
                            // TODO Auto-generated method stub
                            showAuthorize();
                        }
                });
            }
        });
        btnLink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                GDrive.openAuthorizePage();
            }
        });
        btnNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new NewFileDialog(CoProgramming.this);
            }
        });
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent _) {
                int index = fileList.getSelectedIndex();
                if (index != -1) {
                    GDrive.openFilePage(index);
                }
            }
        });
        btnExecute.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent _) {
                int index = fileList.getSelectedIndex();
                if (index != -1) {
                    GDrive.compileExecuteFile(index,
                        new GDriveListener() {
                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onFailure() {
                                if (GDrive.getErrorString() != null) {
                                    new ErrorDialog(GDrive.getErrorString());
                                }
                            }
                    });
                }
            }
        });
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent _) {
                refreshList();
            }
        });
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent _) {
                GDrive.logout();
                showAuthorize();
            }
        });
    }

    void refreshList() {
        GDrive.refreshFileList(new GDriveListener() {
            @Override
            public void onSuccess() {
                fileListModel.clear();
                for (String s : GDrive.getFileTitles()) {
                    fileListModel.addElement(s);
                }
            }

            @Override
            public void onFailure() {
                // TODO Auto-generated method stub

            }
        });
    }
}

class NewFileDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 4485393610539139985L;
    private final CoProgramming parent;
    private JTextField textField;

    /**
     * Create the dialog.
     */
    public NewFileDialog(CoProgramming coPro) {
        parent = coPro;
        setTitle(ResourceBundle.getBundle("net.coprg.coprg.messages")
                .getString("CoProgramming.btnNew.text"));
        getContentPane().setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle(
                        "net.coprg.coprg.messages").getString(
                            "TestDialog.lblName.text"));
            contentPanel.add(lblNewLabel);
        }
        {
            textField = new JTextField();
            contentPanel.add(textField);
            textField.setColumns(10);
            textField.addMouseListener(new ContextMenuHandler());
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(this);
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                cancelButton.addActionListener(this);
                buttonPane.add(cancelButton);
            }
        }
        pack();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent.frmCoprogramming);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            String fileName = textField.getText();
            if (!fileName.isEmpty()) {
                GDrive.createFile(fileName, new GDriveListener() {
                    @Override
                    public void onSuccess() {
                        parent.refreshList();
                    }

                    @Override
                    public void onFailure() {
                        // TODO Auto-generated method stub

                    }
                });
            }
            dispose();
        } else {
            dispose();
        }
    }
}

class ErrorDialog extends JDialog{

    private static final long serialVersionUID = 4990437623650838531L;
    private JTextArea textArea;

    /**
     * Create the dialog.
     */
    public ErrorDialog(String text) {
        setTitle(ResourceBundle.getBundle("net.coprg.coprg.messages")
                .getString("ErrorDialog.title"));
        getContentPane().setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            textArea = new JTextArea(text);
            textArea.setEditable(false);
            contentPanel.add(textArea);
        }
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

class ContextMenuHandler extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
            if (e.getSource() instanceof JTextField) {
                JPopupMenu popup = new JPopupMenu();
                JTextField textField = (JTextField) e.getSource();
                popup.add(textField.getActionMap().get(
                            DefaultEditorKit.pasteAction));
                if (textField.isEditable()
                        && textField.isEnabled()
                        && Toolkit.getDefaultToolkit().getSystemClipboard().getContents(
                            null).isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                            }
            }
        }
    }
}

