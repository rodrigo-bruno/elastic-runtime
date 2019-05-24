package ch.ethz.systems.ert;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;

// TODO - find a way to fire this from jcmd
public class ElasticRuntime {
	private static List<Thread> restartHooks = new ArrayList<>();
	private static Class<?> mainclass;
	private static String[] mainargs;
	
	static class MainThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			while (true) {
				try {
					// Starting main again
					// TODO - call static initializers
					//mainclass.getMethod("<clinit>", Void.class).invoke(null, (Object) mainargs);
		            mainclass.getMethod("main", String[].class).invoke(null, (Object) mainargs);
				} catch (InvocationTargetException ite) {
					Throwable throwable = ite.getTargetException();
					// this is expected for the subsequent invocations of main 
					if (throwable instanceof InterruptedException) {
						continue; // Subsequent times we get here.	
					} 
					// unusual exceptions
					else {
						break;
					}
				}
				// unusual exceptions
				catch (Exception ex) {
					e.printStackTrace();
					break;
				}
			}	
		}
	}
	
	public static void registerRestartHook(Thread hook) {
		restartHooks.add(hook);
	}

	public static void registerMainInvocation(Class<?> klass, String[] args) {
		mainclass = klass;
		mainargs = args;
	}
	
	private static void terminateThread(Thread t, boolean join) throws Exception {
		t.interrupt();
			
		if (join) {
			t.join(100);
			
			if (t.isAlive()) {
				t.stop();
				t.join();
			}
			
		}
	}

    public static void restart(boolean gc) {
    	new Thread() {
    		
    		public void run() {
    			try {
    				Thread main = null;
    				
    	        	// Running a GC.
    				if (gc) {
    					System.gc();	
    				}

    				// Run all restart hooks
    	    		for (Thread hook : restartHooks) {
    	        		hook.start();
    	        		hook.join();
    	        	}
    	    		
    	    		// Restart hooks need to be cleared
    	    		restartHooks.clear();
    	        	
    	    		// Stop all threads
    	        	for (Thread thread : Thread.getAllStackTraces().keySet()) {
    	        		// TODO - these if statements are very fragile!
    	        		if (thread.getId() != Thread.currentThread().getId() && thread.getThreadGroup().getName().equals("main")) {
    	        			if (thread.getName().equals("main")) {
    	        				main = thread;
    	        				main.setUncaughtExceptionHandler(new MainThreadUncaughtExceptionHandler());
    	        			} else {
    	        				terminateThread(thread, true);	
    	        			}
    	        		}
    	        	}

    	        	// stop the main thread (will trigger a new call to main)
    	        	terminateThread(main, false);
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}.start();
    }
}