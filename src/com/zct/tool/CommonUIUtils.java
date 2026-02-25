// 文件路径: D:\图书管理系统\src\com\zct\tool\CommonUIUtils.java
package com.zct.tool;

import com.zct.frame.BookListPanel;
import com.zct.frame.admin_frame;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class CommonUIUtils {
    public static class SimpleDatePicker extends JPanel {
        private JTextField dateField;
        private int year, month;

        public SimpleDatePicker(JTextField field) {
            this.dateField = field;
            setLayout(new BorderLayout());
            JButton datePickerBtn = new JButton(); // 可以设置图标
            datePickerBtn.setOpaque(true);
            datePickerBtn.setBorder(null);
            datePickerBtn.setFocusPainted(false);
            datePickerBtn.setBackground(Color.white);

            // 加载日历图标（可选）
            BufferedImage imageUrl = ImageUtils.loadAndResizeImage(BookListPanel.class, "/image/calendar.png", 30, 30);
            if (imageUrl != null) {
                datePickerBtn.setIcon(new ImageIcon(imageUrl));
            }

            datePickerBtn.addActionListener(e -> showCalendarPopup());
            add(datePickerBtn, BorderLayout.EAST);
        }

        private void showCalendarPopup() {
            JDialog dialog = new JDialog((Frame) null, "选择日期", true);
            dialog.setLayout(new BorderLayout());

            JPanel calendarPanel = new JPanel(new GridLayout(7, 7));
            String[] days = {"日", "一", "二", "三", "四", "五", "六"};
            for (String day : days) {
                JLabel label = new JLabel(day, SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.black));
                calendarPanel.add(label);
            }

            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);

            updateCalendar(cal, calendarPanel, dialog);

            JButton prevBtn = new JButton("<");
            JButton nextBtn = new JButton(">");
            JButton okBtn = new JButton("确定");

            JPanel controlPanel = new JPanel(new BorderLayout());
            JLabel monthLabel = new JLabel(getMonthName(month) + " " + year, SwingConstants.CENTER);
            controlPanel.add(prevBtn, BorderLayout.WEST);
            controlPanel.add(monthLabel, BorderLayout.CENTER);
            controlPanel.add(nextBtn, BorderLayout.EAST);

            dialog.add(controlPanel, BorderLayout.NORTH);
            dialog.add(calendarPanel, BorderLayout.CENTER);
            dialog.add(okBtn, BorderLayout.SOUTH);

            prevBtn.addActionListener(e -> {
                if (month == 0) {
                    month = 11;
                    year--;
                } else {
                    month--;
                }
                monthLabel.setText(getMonthName(month) + " " + year);
                updateCalendar(Calendar.getInstance(), calendarPanel, dialog);
            });

            nextBtn.addActionListener(e -> {
                if (month == 11) {
                    month = 0;
                    year++;
                } else {
                    month++;
                }
                monthLabel.setText(getMonthName(month) + " " + year);
                updateCalendar(Calendar.getInstance(), calendarPanel, dialog);
            });

            okBtn.addActionListener(e -> {
                dialog.dispose();
            });

            dialog.setSize(300, 280);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        private void updateCalendar(Calendar baseCal, JPanel panel, JDialog dialog) {
            panel.removeAll();

            Calendar cal = (Calendar) baseCal.clone();
            cal.set(year, month, 1);

            int startDay = cal.get(Calendar.DAY_OF_WEEK);
            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 1; i < startDay; i++) {
                panel.add(new JLabel(""));
            }

            for (int day = 1; day <= daysInMonth; day++) {
                JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
                dayLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                int finalDay = day;
                dayLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String selectedDate = year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", finalDay);
                        dateField.setText(selectedDate);
                        dialog.dispose();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        dayLabel.setBackground(Color.LIGHT_GRAY);
                        dayLabel.setOpaque(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        dayLabel.setBackground(null);
                        dayLabel.setOpaque(false);
                    }
                });
                panel.add(dayLabel);
            }

            dialog.revalidate();
            dialog.repaint();
        }

        private String getMonthName(int month) {
            String[] months = {"一月", "二月", "三月", "四月", "五月", "六月",
                    "七月", "八月", "九月", "十月", "十一月", "十二月"};
            return months[month];
        }
    }

    //设置滚动条样式
    public static void configureVerticalScrollBar(JScrollPane scrollPane) {
        // 设置滚动速度
        scrollPane.getVerticalScrollBar().setUnitIncrement(13);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        verticalScrollBar.setMinimumSize(new Dimension(8, 20));
        verticalScrollBar.setBackground(Color.LIGHT_GRAY);

        verticalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.GRAY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }
        });
    }

    private static JButton createInvisibleButton() {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(0, 0);
            }
        };
    }

    /**
     * 创建一个带左侧图标线的标题面板
     */
    public static JPanel createTitlePanel(String title) {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));

        // 获取字体高度以设置图标宽度
        FontMetrics fm = titleLabel.getFontMetrics(titleLabel.getFont());
        int fontHeight = fm.getHeight();

        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(10, fontHeight));
        iconLabel.setMaximumSize(new Dimension(10, fontHeight));
        iconLabel.setMinimumSize(new Dimension(10, fontHeight));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(Color.BLACK);

        titlePanel.add(iconLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        titlePanel.add(titleLabel);
        titlePanel.setOpaque(true);
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return titlePanel;
    }

}
