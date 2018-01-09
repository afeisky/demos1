package afei.demo.stdemo;

import java.io.Serializable;

/**
 * Created by chaofei on 17-12-18.
 */

public class DataCallback  implements Serializable {
    public int type=0;
    public String comment="";
    public int percent=0;

    public DataCallback(int _type, String _comment, int _percent){
        type=_type;
        comment=_comment;
        percent=_percent;
    }

}
