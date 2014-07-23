
package com.dsdev.mupdate;

import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author Diamond Sword Development
 */
public abstract class SimpleSwingWorker extends SwingWorker {
    
    @Override
    protected void done() { }

    @Override
    protected void process(List chunks) { }

    @Override
    protected Object doInBackground() {
        task();
        return null;
    }
    
    protected abstract void task();
    
}
