import java.io.*;
import java.util.*;
import java.security.MessageDigest;
public class freeMemory {

	
//---------------------------------------------------------------------------------------
	
	   public static void main(String args[]) {
	      //taking current working directory in consideration
	           final File folder = new File(System.getProperty("user.dir"));
	           try {
	        	  
	       	listFilesForFolder(folder);
	      }
	           catch(Exception e){
	        	   System.out.println("Incorrect position!");
	           }
	       	
	}		
	   
//-------------------------------------------------------------------------------------------
	   //Deleting duplicates
	   public static void deleteDup(HashMap<String,String> rval){

		@SuppressWarnings("rawtypes")
		Iterator iterator = rval.keySet().iterator();
		   
		   Set<String> mySet = new HashSet<String>();
		    while (iterator.hasNext()) {
		       String key = iterator.next().toString();
		       String value = rval.get(key).toString();
		       
		       if (!mySet.add(value))
		        {
		    	   
		    	   File file = new File(key);
		        	
		    		if(file.delete()){
		    			System.out.println(file.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
		            iterator.remove();               
		        }
		       
		       
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