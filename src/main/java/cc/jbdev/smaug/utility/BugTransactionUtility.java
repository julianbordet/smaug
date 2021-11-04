package cc.jbdev.smaug.utility;

import cc.jbdev.smaug.entity.Bug;
import cc.jbdev.smaug.entity.BugTransaction;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BugTransactionUtility {

    public BugTransactionUtility() {
    }

    public void createTransaction(Bug theBug, UserUtility userUtility){

        Date today = new Date();
        String todayInString;
        todayInString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today);

        BugTransaction newBugTransaction = new BugTransaction();
        newBugTransaction.setDate(todayInString);
        newBugTransaction.setTransaction("Bug created");
        newBugTransaction.setTransactionId(0);
        newBugTransaction.setTransactionCreatedBy(userUtility.getMyUserName());
        newBugTransaction.setTransactionDetail("Bug created");
        theBug.addBugTransactions(newBugTransaction);
    }
}
