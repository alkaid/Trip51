package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.account.Account;
import com.alkaid.trip51.model.account.OpenInfo;

/**
 * Created by alkaid on 2015/11/23.
 */
public class ResLogin extends ResponseData{
    private OpenInfo openinfo;
    private Account account;

    public OpenInfo getOpeninfo() {
        return openinfo;
    }

    public void setOpeninfo(OpenInfo openinfo) {
        this.openinfo = openinfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
