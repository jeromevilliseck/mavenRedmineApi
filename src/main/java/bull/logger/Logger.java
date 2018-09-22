package bull.logger;

import bull.App;

public class Logger {

    private static org.apache.log4j.Logger instance;

    private Logger(){
        instance = org.apache.log4j.Logger.getLogger(App.class);
    }

    public static org.apache.log4j.Logger getInstance(){

        if (instance == null)
        {
            new Logger();
        }
        return instance;
    }
}
