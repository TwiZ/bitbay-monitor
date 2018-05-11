package pl.baranski.bitbay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class BitBay {

    private static ApplicationContext context;

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.error("Unable to set LookAndFeel", e);
            throw new RuntimeException(e);
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);
        MainFrame mainFrame = (MainFrame) getContext().getBean("mainFrame");
        mainFrame.init();
        mainFrame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width / 2 - mainFrame.getSize().width / 2,
                dim.height / 2 - mainFrame.getSize().height / 2);
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            context = new AnnotationConfigApplicationContext(SpringContext.class);
        }
        return context;
    }
}
