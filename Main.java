public class Main {
    public static void main(String[] args) {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellValue("A1",3);
        sheet.setCellValue("A2","=A1+A1");
        sheet.setCellValue("A3","=A1-A2*A1");
        sheet.setCellValue("A4","=A1*A2^A1");
        sheet.setCellValue("A5","=A1+A3");
        int val = sheet.getCellValue("A5");
        System.out.println("Values here");

        System.out.println(val);
    }
}


