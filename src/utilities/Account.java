package utilities;

import java.io.Serializable;

public class Account implements Serializable {
    private String accountName;
    private String accountPassword;
    private String accountEmail;

    public Account(String accountName, String accountPassword) {
        this.accountName = accountName;
        this.accountPassword = accountPassword;
    }

    public Account(String accountName, String accountEmail, String accountPassword) {
        this.accountName = accountName;
        this.accountEmail = accountEmail;
        this.accountPassword = accountPassword;
    }

    public String getAccountName() {return accountName;}
    public String getAccountPassword() {return accountPassword;}
    public String getAccountEmail() {return accountEmail;}
}
