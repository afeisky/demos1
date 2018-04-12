package afei.api;

/**
 * Created by chaofei on 18-1-23.
 */

public class TBK {
    public static final String TABLE_NAME = "snbk";

    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_VOLUME = "volume";
    public static final String COLUMN_AMOUNT= "amount";
    public static final String COLUMN_TRADE = "trade";
    public static final String COLUMN_CHANGEPRICE = "chgprice";
    public static final String COLUMN_CHANGEPERCENT = "chgpercent";
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_SNAME = "sname";
    public static final String COLUMN_STRADE = "strade";
    public static final String COLUMN_SCHANGEPRICE = "scgprice";
    public static final String COLUMN_SCHANGEPERCENT = "scgpercent";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_CREATE = "ctime";

    public String code="";
    public String date="";
    public String name="";
    public double number=0.000;
    public double count=0.000;
    public long volume=0;
    public long amount=0;
    public double trade=0.000;
    public double changeprice=0.000;
    public double changepercent=0;
    public String symbol="";
    public String sname="";
    public double strade=0.000;
    public double schangeprice=0.000;
    public double schangepercent=0.000;

    public int createTable(){
        String sql="";
        return 0;
    }
}
