package ro.nn.qa.business;

import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;
import org.tn5250j.tools.LangTool;
import org.tn5250j.tools.logging.TN5250jLogFactory;
import org.tn5250j.tools.logging.TN5250jLogger;
import ro.nn.qa.automation.terminal.Terminal;

import static java.lang.Thread.sleep;

/**
 * Created by Alexandru Giurovici on 18.09.2015.
 */
public class BusinessObjectX extends Screen5250
{
    protected Terminal terminal;
    protected Screen5250 screen;
    protected TN5250jLogger log = TN5250jLogFactory.getLogger(this.getClass());

    public Terminal getTerminal()
    {
        return terminal;
    }

    public Screen5250 getScreen()
    {
        return screen;
    }

    public BusinessObjectX()
    {
        // explicitly disallow construction through parameterless constructor
        terminal = null;
        screen = null;
    }

    public BusinessObjectX(BusinessObjectX owner)
    {
        this.terminal = owner.getTerminal();
        this.screen = owner.getScreen();
    }

    protected void enter() {
        try {
            screen.repaintScreen();
            sleep(2000);
        } catch (InterruptedException e) {
            log.warn(e.getCause());
        }
        screen.sendKeys("[enter]");
    }

    protected void send(String chars, int numTabs)
    {
        if (chars.length() > 0)
        {
            screen.sendKeys(chars);
        }

        for (int i = 0; i < numTabs; i++)
        {
            screen.sendKeys("[tab]");
        }

        try
        {
            sleep(250);
        }
        catch (InterruptedException e)
        {
            log.warn(e.getMessage());
        }

        screen.repaintScreen();
    }

    protected void send(String chars) {
        send(chars, 1);
    }

    public BusinessObjectX(Terminal term) throws InterruptedException
    {
        LangTool.init();
        screen = term.startNewSession().getSession().getScreen();
        sleep(5000);
        terminal = term;
    }

    public MasterMenuX login(String env, String user, String pass)
    {
        ScreenField[] fields = screen.getScreenFields().getFields();
        try {
            fields[0].setString(user);
            fields[1].setString(pass);
            enter();
        } catch (NullPointerException e) {
            throw new RuntimeException("Cannot find login fields");
        }
        enter();
        screen.sendKeys(env);
        enter();

        return new MasterMenuX(this);
    }



}
