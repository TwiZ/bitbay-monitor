package pl.baranski.bitbay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

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
        //        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"Spring-AutoScan.xml"});
        MainFrame mainFrame = (MainFrame) getContext().getBean("mainFrame");
        mainFrame.init();
        mainFrame.setVisible(true);
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            context = new AnnotationConfigApplicationContext(SpringContext.class);
        }
        return context;
    }
}
