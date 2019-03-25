package EventConverter;

import java.util.Iterator;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class TestParser{

	public static void main(String args[]){
		try{
			Parser ps = new Parser();
                

			System.out.println("---Original State List---");
                	for(Iterator<String> iterator = ps.get_original_state_list().keySet().iterator(); iterator.hasNext(); ) {
                        	String key = iterator.next();
                        	System.out.println(key + ": " + ps.get_original_state_list().get(key) );
                	}

                	System.out.println("\n---Original Event RefID List---");
                	for(Iterator<String> iterator = ps.get_original_event_refid_list().keySet().iterator(); iterator.hasNext(); ) {
                        	String key = iterator.next();
                        	System.out.println(key + ": " + ps.get_original_event_refid_list().get(key) );
                	}

                	System.out.println("\n---Original Event List---");
                	for(Iterator<String> iterator = ps.get_original_event_list().keySet().iterator(); iterator.hasNext(); ) {
                        	String key = iterator.next();
                        	System.out.println(key + ": " + Arrays.toString(ps.get_original_event_list().get(key)) );
                	}

                	System.out.println("\n---New State List---");
                	for(Iterator<String> iterator = ps.get_new_state_list().keySet().iterator(); iterator.hasNext(); ) {
                        	String key = iterator.next();
                        	System.out.println(key + ": " + ps.get_new_state_list().get(key) );
                	}

                	System.out.println("\n---New Event RefID List---");
                	for(Iterator<String> iterator = ps.get_new_event_refid_list().keySet().iterator(); iterator.hasNext(); ) {
                        	String key = iterator.next();
                        	System.out.println(key + ": " + ps.get_new_event_refid_list().get(key) );
                	}

                	System.out.println("\n---New Event List---");
                	for(Iterator<String> iterator = ps.get_new_event_list().keySet().iterator(); iterator.hasNext(); ) {
                        	String key = iterator.next();
                        	System.out.println(key + ": " + Arrays.toString(ps.get_new_event_list().get(key)) );
               		 }
		}catch(Exception e){
			System.out.println(e);
		}	
	}

}
