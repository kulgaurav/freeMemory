import java.awt.EventQueue;
import java.io.*;
import java.util.*;
import java.security.MessageDigest;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JProgressBar;


public class gui {

	private JFrame frame;
	private final JLabel lblWarkulock = new JLabel("warkulock");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui window = new gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().setForeground(Color.GRAY);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton JBstart = new JButton("Start");
		JBstart.setBackground(new Color(0, 204, 51));
		JBstart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 final File folder = new File(System.getProperty("user.dir"));
		           try {
		        	  
		       	listFilesForFolder(folder);
		      }
		           catch(Exception e){
		        	   JOptionPane.showMessageDialog(null,"Incorrect position of JAR!");
		           }
			}
		});
		JBstart.setBounds(156, 107, 117, 50);
		frame.getContentPane().add(JBstart);
		
		JButton JBexit = new JButton("Exit");
		JBexit.setBackground(new Color(255, 51, 51));
		JBexit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		JBexit.setBounds(156, 196, 117, 25);
		frame.getContentPane().add(JBexit);
		lblWarkulock.setFont(new Font("Courier 10 Pitch", Font.BOLD, 10));
		lblWarkulock.setBounds(375, 239, 180, 33);
		frame.getContentPane().add(lblWarkulock);
		
		JLabel JLtitle = new JLabel("freeMemory");
		JLtitle.setFont(new Font("Abyssinica SIL", Font.PLAIN, 20));
		JLtitle.setForeground(new Color(204, 51, 102));
		JLtitle.setBounds(150, 0, 123, 50);
		frame.getContentPane().add(JLtitle);
		
		JLabel lbltag = new JLabel("Remove duplicates from directory");
		lbltag.setBounds(109, 34, 263, 50);
		frame.getContentPane().add(lbltag);
	}
	
	
//-------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------
	   //Deleting duplicates
	   public static void deleteDup(HashMap<String,String> rval){

		@SuppressWarnings("rawtypes")
		Iterator iterator = rval.keySet().iterator();
		List<String> stock = new ArrayList<String>();
		   Set<String> mySet = new HashSet<String>();
		   int flag=0;
		    while (iterator.hasNext()) {
		       String key = iterator.next().toString();
		       String value = rval.get(key).toString();
		       
		       if (!mySet.add(value))
		        {
		    	   
		    	   File file = new File(key);
		        	
		    		if(file.delete()){
		    			String name_with_del=file.getName();
		    			stock.add(name_with_del);
		    			//JOptionPane.showMessageDialog(null,file.getName() + " is deleted!");
		    			flag++;
		    		}else{
		    			JOptionPane.showMessageDialog(null,"Delete operation is failed.");
		    		}
		            iterator.remove();               
		        }
		       
		       
		    }
		    //String[] stockArr = new String[stock.size()];
		    //stockArr = stock.toArray(stockArr);
		    if(flag==0){
		    	JOptionPane.showMessageDialog(null,"No duplicate file!");
		    	System.exit(0);
		    }
		    else
		    {
		    	stock.add("\n");
		    	stock.add("\n");
		    	String s = "Number of files deleted:   " + flag;
		    	stock.add(s);
		    	JOptionPane.showMessageDialog(null,new JScrollPane(new JList(stock.toArray())), "Duplicate Files", JOptionPane.INFORMATION_MESSAGE);
		    	System.exit(0);
		    	
		    	
		    }
	   }
	   
//------------------------------------------------------------------------------------------
	   //creating new hashmap with same MD5hash
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void  getDuplicateValues(HashMap<String,String> in)
	{
	   in = (HashMap)in.clone();
	   HashMap<String,String> rval = new HashMap<String,String>();
	   Object[] keys = in.keySet().toArray();

	   for(int x=0;x<keys.length;x++) {
	      Object value = in.get(keys[x]);
	      in.remove(keys[x]);
	      
	      if(in.containsValue(value)) {
	         rval.put((String)(keys[x]),(String)value);
	      }
	      
	      if(rval.containsValue(value)) {
	         rval.put((String)keys[x],(String)value);
	      }
	   }
	   deleteDup(rval);
	
	}

//-------------------------------------------------------------------------------------
	
	//populating hashmap with <name,MD5hash>
	public static void listFilesForFolder(final File folder) {
		HashMap<String, String> hmap = new HashMap<String, String>();
	
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            continue;
	        } else {
	        	String fileNaam=fileEntry.getName();
	        	//not considering hidden files
	        	if(fileNaam.charAt(0)!='.')
					try {
						hmap.put(fileNaam,getMD5Checksum(fileNaam));
					} catch (Exception e) {
						e.printStackTrace();
					}
	        }
	    }
	 	
	    getDuplicateValues(hmap);
	    
	}

//---------------------------------------------------------------------------------------------	

public static byte[] createChecksum(String filename) throws Exception {
    InputStream fis =  new FileInputStream(filename);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }
    } while (numRead != -1);

    fis.close();
    return complete.digest();
}

//----------------------------------------------------------------------------------------------------------

public static String getMD5Checksum(String filename) throws Exception {
    byte[] b = createChecksum(filename);
    String result = "";

    for (int i=0; i < b.length; i++) {
        result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    }
    return result;
}
}

