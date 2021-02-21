package beacon.bof;

import aggressor.AggressorClient;
import beacon.PostExInlineObject;
import common.CommonUtils;
import common.SleevedResource;

public class MyTestBOF extends PostExInlineObject {
    public MyTestBOF(AggressorClient var1) {
        super(var1);
    }

    @Override
    public byte[] getObjectFile(String var1) {
        return CommonUtils.readResource("CustomResources/BOF/MyTestBOF.o");
    }
}
