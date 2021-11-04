package cc.jbdev.smaug.utility;

import cc.jbdev.smaug.entity.BugTransaction;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BugTransactionComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {

        BugTransaction transaction1 = (BugTransaction) obj1;
        BugTransaction transaction2 = (BugTransaction) obj2;

        String transaction1CreationDate = transaction1.getDate();
        String transaction2CreationDate = transaction2.getDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dateForTransaction1 = null;
        Date dateForTransaction2 = null;

        try{
            dateForTransaction1 = simpleDateFormat.parse(transaction1CreationDate);
            dateForTransaction2 = simpleDateFormat.parse(transaction2CreationDate);
        } catch (Exception exc){
            exc.printStackTrace();
        }

        if(dateForTransaction1.before(dateForTransaction2)){
            return -1;
        }

        else if (dateForTransaction2.before(dateForTransaction1)){
            return 1;
        }

        else {
            return 0;
        }
    }
}
