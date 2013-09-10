package com.midtrans.bank.logic.transaction;

import com.midtrans.bank.core.model.SettlementTxn;
import com.midtrans.bank.core.transaction.BankTxnSupport;
import com.midtrans.bank.logic.dao.impl.SettlementTxnDao;
import org.jpos.ee.DB;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;

/**
 * Created with IntelliJ IDEA.
 * User: shaddiqa
 * Date: 9/10/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveSettlementTxn extends BankTxnSupport implements AbortParticipant {
    SettlementTxnDao dao;

    @Override
    protected int doPrepare(long id, Context ctx) throws Exception {
        return saveSettlementTxn(ctx);
    }

    @Override
    protected int doPrepareForAbort(long id, Context ctx) throws Exception {
        if(ctx.get(SETTLE_TXN) == null) {
            return PREPARED | NO_JOIN;
        }

        return saveSettlementTxn(ctx);
    }

    private int saveSettlementTxn(Context ctx) {
        DB db = openDB(ctx);

        dao = new SettlementTxnDao(db);

        SettlementTxn settlementTxn = (SettlementTxn) ctx.get(SETTLE_TXN);

        dao.saveOrUpdate(settlementTxn);

        closeDB(ctx);
        return PREPARED | NO_JOIN;
    }
}
