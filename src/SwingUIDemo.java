import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class SwingUIDemo extends JFrame {
    public SwingUIDemo() {
        super("Swing UI demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(400, 100, 800, 600);
        setResizable(false);

        //Меню вверху экрана____________________________________________________
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(ElementCreator.createFileMenu());
        menuBar.add(ElementCreator.createStateMenu());
        menuBar.add(ElementCreator.createSettingsMenu());
        setJMenuBar(menuBar);
        //______________________________________________________________________

        //Вкладки States _________________________________________
        JTabbedPane stateTabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        stateTabPane.setPreferredSize(new Dimension(1600, 800));
        // Создание вкладок
        for (int i = 1; i < 10; i++) {
            JPanel panel = new JPanel();
            panel.add(new JButton("State " + i));
            panel.setName("State " + i);
            stateTabPane.addTab("State " + i, new ImageIcon(), panel, "Press alt + " + i);
            stateTabPane.setMnemonicAt(i - 1, String.valueOf(i).charAt(0));
        }

        //Отображение пути к компоненту, с которым мы сейчас работаем
        JLabel componentPath = new JLabel("Frame");
        componentPath.setFont(new Font("Calibri", Font.ITALIC, 12));

        //Панелька со вкладками States
        JPanel stateTabPanel = new JPanel(new VerticalLayout());
        stateTabPanel.setBounds(0, 0, 600, 800);
        stateTabPanel.add(componentPath);
        stateTabPane.addChangeListener(e -> componentPath.setText("Frame -> " + ((JTabbedPane) e.getSource()).getSelectedComponent().getName()));
        stateTabPanel.add(stateTabPane);

        //Панелька со вкладками States(Прокрутка)
        JScrollPane stateTabScrollPane = new JScrollPane(stateTabPanel);

        //______________________________________________________________________

        //Дерево States_________________________________________________________
        JTree stateTree = ElementCreator.createStateTree(7);
        stateTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = ((DefaultMutableTreeNode) stateTree.getLastSelectedPathComponent());

            if (selected.getParent().equals(stateTree.getModel().getRoot()))
                stateTabPane.setSelectedIndex(Integer.parseInt(selected.toString().split(" ")[1]) - 1);
        });

        //Панелька с деревом(Прокрутка)
        JScrollPane stateScrollPane = new JScrollPane(stateTree);
        stateScrollPane.setSize(new Dimension(160, 500));
        //______________________________________________________________________

        //Панелька скриптов_____________________________________________________
        JPanel scriptTabPanel = new JPanel(new VerticalLayout());
        scriptTabPanel.add(new JLabel("Scripts"));

        //Панелька со вкладками скриптов
        JTabbedPane scriptsTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        scriptsTabbedPane.setPreferredSize(new Dimension(590, 500));
        // Создание вкладок
        for (int i = 1; i < 10; i++) {
            JPanel panel = new JPanel();
            panel.add(new JTextArea("Script " + i));
            panel.setName("Script " + i);
            scriptsTabbedPane.addTab("Script " + i, new ImageIcon(), panel, "Press alt + " + i);
        }
        scriptTabPanel.add(scriptsTabbedPane);

        //Панелька со вкладками скриптов(прокрутка)
        JScrollPane scriptScrollPane = new JScrollPane(scriptTabPanel);
        //______________________________________________________________________

        //разделитель между скриптами и стейтами________________________________
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        verticalSplit.setDividerLocation(300);
        verticalSplit.setOneTouchExpandable(true);
        verticalSplit.setTopComponent(stateTabScrollPane);
        verticalSplit.setBottomComponent(scriptScrollPane);
        //ограничения разделителя
        verticalSplit.addPropertyChangeListener(e -> {
            int dividerLocation = verticalSplit.getDividerLocation();

            if (dividerLocation > 400) {
                verticalSplit.setDividerLocation(400);
            }
            if (dividerLocation < 200) {
                verticalSplit.setDividerLocation(200);
            }
        });
        //______________________________________________________________________

        //Разделитель между панелькой с деревом и панельками со скриптами и state ами
        JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        horizontalSplit.setDividerLocation(140);
        horizontalSplit.setOneTouchExpandable(true);
        horizontalSplit.setLeftComponent(stateScrollPane);
        horizontalSplit.setRightComponent(verticalSplit);
        //ограничения разделителя
        horizontalSplit.addPropertyChangeListener(e -> {
            int dividerLocation = horizontalSplit.getDividerLocation();
            System.out.println("Divider: " + dividerLocation);

            if (dividerLocation > 220) {
                horizontalSplit.setDividerLocation(220);
            }
            if (dividerLocation < 163) {
                horizontalSplit.setDividerLocation(163);
            }
        });
        //______________________________________________________________________

        getContentPane().add(horizontalSplit);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SwingUIDemo();
    }
}

//создание меню и дерева (позже вынесу сюда остальное)
class ElementCreator {
    //создание меню с несколькими элементами
    private static JMenu createJMenu(String name, JMenuItem... items) {
        JMenu res = new JMenu(name);

        for (int i = 0; i < items.length; i++) {
            res.add(items[i]);
            if (i < items.length - 1)
                res.addSeparator();
        }
        return res;
    }

    //создание элемента меню
    private static JMenuItem createJMenuItem(String text, ImageIcon icon, ActionListener e) {
        JMenuItem item = new JMenuItem();
        item.setText(text);
        item.setIcon(icon);
        item.addActionListener(e);
        return item;
    }

    //создание меню states
    public static JMenu createStateMenu() {
        JMenuItem state1 = createJMenuItem(
                "State 1",
                new ImageIcon("images/state.png"),
                e -> System.out.println("ActionListener.actionPerformed : state1")
        );

        JMenuItem state2 = createJMenuItem(
                "State 2",
                new ImageIcon("images/state.png"),
                e -> System.out.println("ActionListener.actionPerformed : state2")
        );
        return createJMenu("State", state1, state2);
    }

    //создание меню settings
    public static JMenu createSettingsMenu() {
        JMenuItem setting1 = createJMenuItem(
                "Setting 1",
                new ImageIcon("images/setting.png"),
                e -> System.out.println("ActionListener.actionPerformed : setting1")
        );

        JMenuItem setting2 = createJMenuItem(
                "Setting 1",
                new ImageIcon("images/setting.png"),
                e -> System.out.println("ActionListener.actionPerformed : setting2")
        );
        return createJMenu("Settings", setting1, setting2);
    }

    //создание меню file
    public static JMenu createFileMenu() {
        JMenuItem open = createJMenuItem(
                "Open",
                new ImageIcon("images/open.png"),
                e -> System.out.println("ActionListener.actionPerformed : open")
        );

        JMenuItem exit = createJMenuItem(
                "Exit",
                new ImageIcon("images/exit.png"),
                e -> System.exit(20)
        );
        return createJMenu("File", open, exit);
    }

    //создание нового стейста
    private static DefaultMutableTreeNode createState(int stateNumber) {
        DefaultMutableTreeNode state = new DefaultMutableTreeNode("State " + stateNumber);

        state.add(new DefaultMutableTreeNode("Component 1", false));
        state.add(new DefaultMutableTreeNode("Component 2", false));
        state.add(new DefaultMutableTreeNode("Component 3", false));
        state.add(new DefaultMutableTreeNode("Component 4", false));
        state.add(new DefaultMutableTreeNode("Component 5", false));
        state.add(new DefaultMutableTreeNode("Component 6", false));
        state.add(new DefaultMutableTreeNode("Component 7", false));
        state.add(new DefaultMutableTreeNode("Component 8", false));
        state.add(new DefaultMutableTreeNode("Component 9", false));

        return state;
    }

    //создание дерева из стейтов
    public static JTree createStateTree(int states) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("States");

        for (int i = 1; i < states + 1; i++) {
            root.add(createState(i));
        }
        DefaultTreeModel stateTreeModel = new DefaultTreeModel(root, true);
        return new JTree(stateTreeModel);
    }
}

// Менеджер вертикального расположения компонентов (писал не я, а взял с того сайта)
class VerticalLayout implements LayoutManager {
    private final Dimension size = new Dimension();

    // Следующие два метода не используются
    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    // Метод определения минимального размера для контейнера
    public Dimension minimumLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    // Метод определения предпочтительного размера для контейнера
    public Dimension preferredLayoutSize(Container c) {
        return calculateBestSize(c);
    }

    // Метод расположения компонентов в контейнере
    public void layoutContainer(Container container) {
        // Список компонентов
        Component[] list = container.getComponents();
        int currentY = 5;
        for (Component component : list) {
            // Определение предпочтительного размера компонента
            Dimension pref = component.getPreferredSize();
            // Размещение компонента на экране
            component.setBounds(5, currentY, pref.width, pref.height);
            // Учитываем промежуток в 5 пикселов
            currentY += 5;
            // Смещаем вертикальную позицию компонента
            currentY += pref.height;
        }
    }

    // Метод вычисления оптимального размера контейнера
    private Dimension calculateBestSize(Container c) {
        // Вычисление длины контейнера
        Component[] list = c.getComponents();
        int maxWidth = 0;
        for (int i = 0; i < list.length; i++) {
            int width = list[i].getWidth();
            // Поиск компонента с максимальной длиной
            if (width > maxWidth)
                maxWidth = width;
        }
        // Размер контейнера в длину с учетом левого отступа
        size.width = maxWidth + 5;
        // Вычисление высоты контейнера
        int height = 0;
        for (int i = 0; i < list.length; i++) {
            height += 5;
            height += list[i].getHeight();
        }
        size.height = height;
        return size;
    }
}

