package common;

public class Authorization {
   protected int watermark = 0;
   protected String validto = "";
   protected String error = null;
   protected boolean valid = false;

   public Authorization() {
      try {
         byte[] decrypt = new byte[]{1, -55, -61, 127, 0, 0, 0, 0, 100, 1, 0, 27, -27, -66, 82, -58, 37, 92, 51, 85, -114, -118, 28, -74, 103, -53, 6};
         DataParser dataParser = new DataParser(decrypt);
         dataParser.big();
         int int1 = dataParser.readInt();
         this.watermark = dataParser.readInt();
         if (dataParser.readByte() < 41) {
            this.error = "Authorization file is not for Cobalt Strike 4.1+";
            return;
         }

         int i1 = dataParser.readByte();
         dataParser.readBytes(i1);
         byte[] bytes = dataParser.readBytes(dataParser.readByte());
         if (29999999 == int1) {
            this.validto = "forever";
            MudgeSanity.systemDetail("valid to", "perpetual");
         } else {
            this.validto = "20" + int1;
            MudgeSanity.systemDetail("valid to", CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()));
         }

         this.valid = true;
         MudgeSanity.systemDetail("id", this.watermark + "");
         SleevedResource.Setup(bytes);
      } catch (Exception var7) {
         MudgeSanity.logException("auth file parsing", var7, false);
      }

   }

   public boolean isPerpetual() {
      return "forever".equals(this.validto);
   }

   public boolean isValid() {
      return this.valid;
   }

   public String getError() {
      return this.error;
   }

   public String getWatermark() {
      return this.watermark + "";
   }

   public long getExpirationDate() {
      return CommonUtils.parseDate(this.validto, "yyyyMMdd");
   }

   public boolean isExpired() {
      return System.currentTimeMillis() > this.getExpirationDate() + CommonUtils.days(1);
   }

   public String whenExpires() {
      long n = (this.getExpirationDate() + CommonUtils.days(1) - System.currentTimeMillis()) / CommonUtils.days(1);
      if (n == 1L) {
         return "1 day (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")";
      } else {
         return n <= 0L ? "TODAY (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")" : n + " days (" + CommonUtils.formatDateAny("MMMMM d, YYYY", this.getExpirationDate()) + ")";
      }
   }

   public boolean isAlmostExpired() {
      return System.currentTimeMillis() + CommonUtils.days(30) > this.getExpirationDate();
   }
}
