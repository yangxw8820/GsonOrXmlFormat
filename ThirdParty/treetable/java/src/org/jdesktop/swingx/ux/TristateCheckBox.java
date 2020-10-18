/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.swingx.ux;


import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.event.*;

/**
 * Maintenance tip - There were some tricks to getting this code
 * working:
 * <p>
 * 1. You have to overwite addMouseListener() to do nothing
 * 2. You have to add a mouse event on mousePressed by calling
 * super.addMouseListener()
 * 3. You have to replace the UIActionMap for the keyboard event
 * "pressed" with your own one.
 * 4. You have to remove the UIActionMap for the keyboard event
 * "released".
 * 5. You have to grab focus when the next state is entered,
 * otherwise clicking on the component won't get the focus.
 * 6. You have to make a TristateDecorator as a button model that
 * wraps the original button model and does state management.
 */
public class TristateCheckBox extends JCheckBox {

    private final TristateDecorator decorator;

    private Selector selector;


    public TristateCheckBox(String text, Icon icon, Boolean initial) {
        super(text, icon);
        // Add a listener for when the mouse is pressed
        super.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                grabFocus();
                decorator.nextState();
            }
        });
        // Reset the keyboard action map
        ActionMap map = new ActionMapUIResource();
        map.put("pressed", new AbstractAction() {      //NOI18N

            public void actionPerformed(ActionEvent e) {
                grabFocus();
                decorator.nextState();
            }
        });
        map.put("released", null);                     //NOI18N
        SwingUtilities.replaceUIActionMap(this, map);
        // set the model to the adapted model
        decorator = new TristateDecorator(getModel());
        setModel(decorator);
        setState(initial);
    }

    public TristateCheckBox(String text, Boolean initial) {
        this(text, null, initial);
    }

    public TristateCheckBox(String text) {
        this(text, true);
    }

    public TristateCheckBox() {
        this(null);
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    /**
     * No one may add mouse listeners, not even Swing!
     */
    @Override
    public void addMouseListener(MouseListener l) {
    }

    /**
     * Return the current state, which is determined by the
     * selection status of the model.
     */
    public Boolean getState() {
        return decorator.getState();
    }

    /**
     * Set the new state to either SELECTED, NOT_SELECTED or
     * DONT_CARE.  If state == null, it is treated as DONT_CARE.
     */
    public void setState(Boolean state) {
        decorator.setState(state);
    }

    /**
     * Exactly which Design Pattern is this?  Is it an Adapter,
     * a Proxy or a Decorator?  In this case, my vote lies with the
     * Decorator, because we are extending functionality and
     * "decorating" the original model with a more powerful model.
     */
    private class TristateDecorator implements ButtonModel {

        private final ButtonModel other;

        private TristateDecorator(ButtonModel other) {
            this.other = other;
        }

        /**
         * The current state is embedded in the selection / armed
         * state of the model.
         * <p>
         * We return the SELECTED state when the checkbox is selected
         * but not armed, DONT_CARE state when the checkbox is
         * selected and armed (grey) and NOT_SELECTED when the
         * checkbox is deselected.
         */
        private Boolean getState() {
            if (isSelected() && !isArmed()) {
                // normal black tick
                return Boolean.TRUE;
            } else if (isSelected() && isArmed()) {
                // don't care grey tick
                return null;
            } else {
                // normal deselected
                return Boolean.FALSE;
            }
        }

        private void setState(Boolean state) {
            if (state == Boolean.FALSE) {
                other.setArmed(false);
                if (selector != null) {
                    selector.setSelect(false);
                }
                setPressed(false);
                setSelected(false);
            } else if (state == Boolean.TRUE) {
                other.setArmed(false);
                setPressed(false);
                setSelected(true);
                if (selector != null) {
                    selector.setSelect(true);
                }
            } else {
                other.setArmed(true);
                setPressed(true);
                setSelected(true);
                if (selector != null) {
                    selector.setSelect(true);
                }

            }
        }

        /**
         * We rotate between NOT_SELECTED, SELECTED and DONT_CARE.
         */
        private void nextState() {
            Boolean current = getState();
            if (current == Boolean.FALSE) {
                setState(Boolean.TRUE);
            } else if (current == Boolean.TRUE) {
                setState(null);
            } else if (current == null) {
                setState(Boolean.FALSE);
            }
        }

        public boolean isFocusTraversable() {
            return isEnabled();
        }

        /**
         * All these methods simply delegate to the "other" model
         * that is being decorated.
         */
        public boolean isArmed() {
            return other.isArmed();
        }

        /**
         * Filter: No one may change the armed status except us.
         */
        public void setArmed(boolean b) {
        }

        public boolean isSelected() {
            return other.isSelected();
        }

        public void setSelected(boolean b) {
            other.setSelected(b);
        }

        public boolean isEnabled() {
            return other.isEnabled();
        }

        /**
         * We disable focusing on the component when it is not
         * enabled.
         */
        public void setEnabled(boolean b) {
//            setFocusable(b);
            other.setEnabled(b);
        }

        public boolean isPressed() {
            return other.isPressed();
        }

        public void setPressed(boolean b) {
            other.setPressed(b);
        }

        public boolean isRollover() {
            return other.isRollover();
        }

        public void setRollover(boolean b) {
            other.setRollover(b);
        }

        public int getMnemonic() {
            return other.getMnemonic();
        }

        public void setMnemonic(int key) {
            other.setMnemonic(key);
        }

        public String getActionCommand() {
            return other.getActionCommand();
        }

        public void setActionCommand(String s) {
            other.setActionCommand(s);
        }

        public void setGroup(ButtonGroup group) {
            other.setGroup(group);
        }

        public void addActionListener(ActionListener l) {
            other.addActionListener(l);
        }

        public void removeActionListener(ActionListener l) {
            other.removeActionListener(l);
        }

        public void addItemListener(ItemListener l) {
            other.addItemListener(l);
        }

        public void removeItemListener(ItemListener l) {
            other.removeItemListener(l);
        }

        public void addChangeListener(ChangeListener l) {
            other.addChangeListener(l);
        }

        public void removeChangeListener(ChangeListener l) {
            other.removeChangeListener(l);
        }

        public Object[] getSelectedObjects() {
            return other.getSelectedObjects();
        }
    }
}