package cc.jbdev.smaug.entity;

import javax.persistence.*;

@Entity
@Table(name="bug_transaction")
public class BugTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transaction_id")
    private int transactionId;

    @Column(name="date")
    private String date;

    @Column(name="transaction")
    private String transaction;

   /* @Column(name="bug_id")
    private int bugId;
*/
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="bug_id")
    private Bug bugId;

    public BugTransaction() {
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Bug getBugId() {
        return bugId;
    }

    public void setBugId(Bug bugId) {
        this.bugId = bugId;
    }
}
