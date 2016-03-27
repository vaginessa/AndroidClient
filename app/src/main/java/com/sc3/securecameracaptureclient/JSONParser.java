package com.sc3.securecameracaptureclient;

import java.util.regex.Pattern;

/**
 * Created by Nathan on 3/27/2016.
 */
public class JSONParser {
    public JSONObject jO;

    public JSONParser(String JSONfile) {

        String testString = JSONfile;
        String jsonString = testString.replaceAll("\\s+", "");

        //New JsonObject
        jO = new JSONObject();

        int stringLeftToIndex = testString.length();
        int currentPosInString = 0;

        //Pull off the first characters
        currentPosInString += 2;
        stringLeftToIndex -= 2;

        //Set list counters for levels of listing
        int yearLevel = -1;
        int monthLevel = -1;
        int dayLevel = -1;
        int hourLevel = -1;

        //Starting at the year level
        while (stringLeftToIndex > 0) {
            //Get the year
            String year = jsonString.substring(currentPosInString, currentPosInString+4);
            //Update counters
            currentPosInString += 4;
            stringLeftToIndex -= 4;

            //Create Object
            jO.year.add(new Year(Integer.parseInt(year)));
            yearLevel++;
            //Get the month
            currentPosInString += 3;
            stringLeftToIndex -= 3;
            //Reset the month level
            monthLevel = -1;

            while (stringLeftToIndex > 0) {
                String month = "";
                String monthBlock = jsonString.substring(currentPosInString, currentPosInString+10);
                //month = Regex.Match(monthBlock, "\"([^\\)]+)\"").ToString();
                month = Pattern.compile("\"([^\\)]+)\"").split(monthBlock)[0];
                System.out.println(month);
                month = month.replaceAll("\"", "");
                month = month.replaceAll(":", "");
                month = month.replaceAll("\\{", "");
                //Update counters
                currentPosInString += month.length() + 2;
                stringLeftToIndex -= month.length() + 2;

                //Create Object
                jO.year.get(yearLevel).months.add(new Month(getMonthInt(month)));
                monthLevel++;
                //Get the day
                currentPosInString++;
                stringLeftToIndex--;
                //Reset the day level
                dayLevel = -1;

                while (stringLeftToIndex > 0) {
                    currentPosInString++;
                    stringLeftToIndex--;
                    String day = "";
                    String dayBlock = jsonString.substring(currentPosInString, currentPosInString+4);
                    day = Pattern.compile("\"([^\\)]+)\"").split(dayBlock)[0];
                    day = day.replaceAll("\"", "");
                    //Update counters
                    currentPosInString += day.length() + 2;
                    stringLeftToIndex -= day.length() + 2;

                    //Create Object
                    jO.year.get(yearLevel).months.get(monthLevel).days.add(new Day(Integer.parseInt(day)));
                    dayLevel++;
                    //Get the hour
                    currentPosInString += 2;
                    stringLeftToIndex -= 2;
                    //Reset the hour count
                    hourLevel = -1;

                    while (stringLeftToIndex > 0) {
                        String hour = "";
                        String hourBlock = jsonString.substring(currentPosInString, currentPosInString+4);
                        hour = Pattern.compile("\"([^\\)]+)\"").split(hourBlock)[0];
                        if (hour == "") {
                            currentPosInString++;
                            hourBlock = jsonString.substring(currentPosInString, currentPosInString+4);
                            hour = Pattern.compile("\"([^\\)]+)\"").split(hourBlock)[0];
                        }
                        hour = hour.replaceAll("\"", "");

                        //Update counters
                        currentPosInString += hour.length() + 3;
                        stringLeftToIndex -= hour.length() + 3;

                        //Create object
                        jO.year.get(yearLevel).months.get(monthLevel).days.get(dayLevel).hours.add(new Hour(Integer.parseInt(hour)));
                        hourLevel++;

                        int hourBlockEnd = 0;
                        int i = currentPosInString;
                        while (stringLeftToIndex > 0) {
                            if (jsonString.charAt(i) == ']') {
                                break;
                            } else {
                                i++;
                                hourBlockEnd++;
                            }
                        }

                        hourBlock = jsonString.substring(currentPosInString, hourBlockEnd);

                        //Update indexes
                        currentPosInString += hourBlockEnd;
                        stringLeftToIndex -= hourBlockEnd;

                        //Get the objects in the hour block
                        String[] imageMatches = Pattern.compile("\\{([^\\}]+?)\\}").split(hourBlock);

                        //Get first match
                        String minute = "";
                        String date_taken = "";
                        String file_name = "";
                        String method = "";

                        for(String match1 : imageMatches)
                        {
                            try {
                                String match = match1;

                                file_name = Pattern.compile("\"filename\"[ :]+(\"[^\"]*\")").split(match)[0].substring(0,12);
                                date_taken = Pattern.compile("\"datetaken\"[ :]+(\"[^\"]*\")").split(match)[0].substring(0,13);
                                method = Pattern.compile("\"bold\"[ :]+(\"[^\"]*\")").split(match)[0].substring(0,8);

                                file_name = file_name.replaceAll("\"", "");
                                date_taken = date_taken.replaceAll("\"", "");
                                method = method.replaceAll("\"", "");

                                minute = date_taken.substring(date_taken.length()-3, date_taken.length()-1);

                                jO.year.get(yearLevel).months.get(monthLevel).days.get(dayLevel).hours.get(hourLevel).images.add(new Image(Integer.parseInt(minute), file_name, date_taken, getMethodInt(method)));

                            } catch (Exception e){

                            }
                        }
                        currentPosInString++;
                        if (jsonString.charAt(currentPosInString) == ',') {
                            continue;
                        } else {
                            break;
                        }
                    }
                    currentPosInString++;
                    if (jsonString.charAt(currentPosInString) == ',') {
                        //currentPosInString++;
                        continue;
                    } else {
                        break;
                    }
                }
                currentPosInString++;
                if (jsonString.charAt(currentPosInString) == ',') {
                    currentPosInString++;
                    continue;
                } else {
                    break;
                }
            }
            currentPosInString++;
            if (jsonString.charAt(currentPosInString) == ',') {
                currentPosInString += 2;
                continue;
            } else {
                break;
            }
        }

    }

    public int getMonthInt(String month) {
        switch (month) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return -1;
        }

    }

    public int getMethodInt(String method) {
        switch (method) {
            case "timer":
                return 1;
            case "motion":
                return 2;
            default:
                return -1;
        }
    }
}
