package timecounter;

import ch.ethz.systems.ert.ElasticRuntime;

public class TimeCounter {
	
	static {
		System.out.println("I am static initializer!");
		new Throwable().printStackTrace();
	}

	static {
		System.out.println("I am static initializer 2!");
		try {
			Thread.sleep(10000);	
		} catch (Exception e) {
			System.out.println("Exception?");	
		}
		
	}
	
    public static void main(String[] args) throws Exception {
        int limit = 100000;

        Thread timer = new Thread() {

            public void run() {
            	System.out.println("Starting counter thread 1");
                try {
                    for (int i = 0; i < limit; i++) {
                        System.out.println(String.format("1 - %s", i));
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                	// Ignoring but terminating
                }
               
            }
           
        };
        
        Thread timer2 = new Thread() {

            public void run() {
            	System.out.println("Starting counter thread 2");
                
                for (int i = 0; i < limit; i++) {
                    System.out.println(String.format("2 - %d", i));
                    try {
                    	Thread.sleep(500);
                    } catch (InterruptedException e) {
                    	// Ignoring
                    }
                }
            }
           
        };
        
        ElasticRuntime.registerRestartHook(new Thread() {
        	public void run() {
        		System.out.println("I the first restart hook doing nothing!");	
        	}
        });
        
        ElasticRuntime.registerRestartHook(new Thread() {
        	public void run() {
        		System.out.println("I the second restart hook doing nothing!");	
        	}
        });
        
        ElasticRuntime.registerMainInvocation(TimeCounter.class, args);

        timer.start();
        timer2.start();
        Thread.sleep(2000);
        
        long time = System.currentTimeMillis();
        ElasticRuntime.restart(false);
        System.out.println("restart time is " + (System.currentTimeMillis() - time));
        
        Thread.sleep(2000);
        timer.join();
        timer2.join();
    }
}