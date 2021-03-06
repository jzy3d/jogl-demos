package demos.newt;

import com.jogamp.nativewindow.*;
import com.jogamp.newt.*;
import com.jogamp.newt.event.*;
import demos.newt.util.TaskManager;

public class TaskManagerTest1  implements WindowListener, KeyListener, MouseListener
{
    final static TaskManager eventMgr;
    final static TaskManager renderMgr;

    static
    {
        System.setProperty("java.awt.headless", "true");

        eventMgr = new TaskManager("Event Manager");
        eventMgr.start();

        renderMgr = new TaskManager("Render Manager");
        renderMgr.start();
    }

    public static void main(String[] args)
    {
        new TaskManagerTest1().run();
    }

    Window window;
    Display display;

    public void windowRepaint(WindowUpdateEvent e) { 
        System.err.println("windowRepaint "+e);
    }
    public void windowResized(WindowEvent e) {
        System.err.println("windowResized "+e);
    }
    public void windowMoved(WindowEvent e) {
        System.err.println("windowMoved "+e);
    }
    public void windowGainedFocus(WindowEvent e) {
        System.err.println("windowGainedFocus "+e);
    }
    public void windowLostFocus(WindowEvent e) {
        System.err.println("windowLostFocus "+e);
    }
    public void windowDestroyNotify(WindowEvent e) {
        System.err.println("windowDestroyNotify "+e);
        System.err.println("Window Event Listener DestroyNotify send stop request - START");
        renderMgr.stop();
        eventMgr.stop();
        System.err.println("Window Event Listener DestroyNotify send stop request - DONE");
    }
    public void windowDestroyed(WindowEvent e) {
        System.err.println("windowDestroyed "+e);
    }
    public void keyPressed(KeyEvent e) {
        System.err.println("keyPressed "+e);
    }
    public void keyReleased(KeyEvent e) {
        System.err.println("keyReleased "+e);
    }
    public void mouseClicked(MouseEvent e) {
        System.err.println("mouseClicked "+e);
    }
    public void mouseEntered(MouseEvent e) {
        System.err.println("mouseEntered "+e);
    }
    public void mouseExited(MouseEvent e) {
        System.err.println("mouseExited "+e);
    }
    public void mousePressed(MouseEvent e) {
        System.err.println("mousePressed "+e);
    }
    public void mouseReleased(MouseEvent e) {
        System.err.println("mouseReleased "+e);
    }
    public void mouseMoved(MouseEvent e) {
        System.err.println("mouseMoved "+e);
    }
    public void mouseDragged(MouseEvent e) {
        System.err.println("mouseDragged "+e);
    }
    public void mouseWheelMoved(MouseEvent e) {
        System.err.println("mouseWheelMoved "+e);
    }

    void render(long context)
    {

    }

    private class EventThread implements Runnable {
        public void run() {
            try {
                // prolog - lock whatever you need

                // do it ..
                if(null!=display) {
                    display.dispatchMessages();
                }
            } catch (Throwable t) {
                // handle errors ..
                t.printStackTrace();
            } finally {
                // epilog - unlock locked stuff
            }
        }
    }

    private class RenderThread implements Runnable {
        public void run() {
            if(null==window) {
                return;
            }
            try {
                // prolog - lock whatever you need
                window.lockSurface();

                // render(window.getSurfaceHandle());
                System.out.print(".");
                Thread.sleep(100);
            } catch (Throwable t) {
                // handle errors ..
                t.printStackTrace();
            } finally {
                // epilog - unlock locked stuff
                window.unlockSurface();
            }
        }
    }

    void run()
    {
        try
        {

            Capabilities caps = new Capabilities();
            caps.setRedBits(8);
            caps.setGreenBits(8);
            caps.setBlueBits(8);
            //caps.setBackgroundOpaque(true);

            display = NewtFactory.createDisplay(null);
            Screen screen = NewtFactory.createScreen(display, 0);
            window = NewtFactory.createWindow(screen, caps);
            window.setTitle("GlassPrism");
            // window.setHandleDestroyNotify(false);
            window.setUndecorated(false);
            window.setSize(256, 256);
            window.addKeyListener(this);
            window.addMouseListener(this);

            // let's get notified if window is closed
            window.addWindowListener(this); 

            window.setVisible(true);

            eventMgr.addTask(new EventThread());
            renderMgr.addTask(new RenderThread());

            System.out.println("Main - wait until finished");
            renderMgr.waitUntilStopped();
            eventMgr.waitUntilStopped();
            System.out.println("Main - finished");

            window.destroy();
            System.out.println("Main - window destroyed");
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
}

