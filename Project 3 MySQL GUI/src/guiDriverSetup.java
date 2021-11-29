import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.SQLException;

public class guiDriverSetup
{
        public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException 
        {
                mainGUI guiFrame = new mainGUI();
                guiFrame.setVisible(true);
                guiFrame.pack();
                guiFrame.setLayout(new BorderLayout(2,0));
        }
}