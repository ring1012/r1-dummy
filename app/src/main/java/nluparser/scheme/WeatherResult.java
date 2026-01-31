package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class WeatherResult implements Result {

    @SerializedName("cityCode")
    @JSONField(name = "cityCode")
    String cityCode;

    @SerializedName("cityName")
    @JSONField(name = "cityName")
    String cityName;

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    int errorCode;

    @SerializedName("focusDateIndex")
    @JSONField(name = "focusDateIndex")
    int focusDateIndex;

    @SerializedName("updateTime")
    @JSONField(name = "updateTime")
    String updateTime;

    @SerializedName("weatherDays")
    @JSONField(name = "weatherDays")
    List<WeatherDay> weatherDays = Collections.emptyList();

    public static class WeatherDay {

        @SerializedName("carWashIndex")
        @JSONField(name = "carWashIndex")
        String carWashIndex;

        @SerializedName("carWashIndexDesc")
        @JSONField(name = "carWashIndexDesc")
        String carWashIndexDesc;

        @SerializedName("coldIndex")
        @JSONField(name = "coldIndex")
        String coldIndex;

        @SerializedName("coldIndexDesc")
        @JSONField(name = "coldIndexDesc")
        String coldIndexDesc;

        @SerializedName("comfortIndex")
        @JSONField(name = "comfortIndex")
        String comfortIndex;

        @SerializedName("comfortIndexDesc")
        @JSONField(name = "comfortIndexDesc")
        String comfortIndexDesc;

        @SerializedName("currentTemperature")
        @JSONField(name = "currentTemperature")
        String currentTemperature;

        @SerializedName("datingIndex")
        @JSONField(name = "datingIndex")
        String datingIndex;

        @SerializedName("datingIndexDesc")
        @JSONField(name = "datingIndexDesc")
        String datingIndexDesc;

        @SerializedName("day")
        @JSONField(name = "day")
        String day;

        @SerializedName("dayOfWeek")
        @JSONField(name = "dayOfWeek")
        String dayOfWeek;

        @SerializedName("dressIndex")
        @JSONField(name = "dressIndex")
        String dressIndex;

        @SerializedName("dressIndexDesc")
        @JSONField(name = "dressIndexDesc")
        String dressIndexDesc;

        @SerializedName("dryingIndex")
        @JSONField(name = "dryingIndex")
        String dryingIndex;

        @SerializedName("dryingIndexDesc")
        @JSONField(name = "dryingIndexDesc")
        String dryingIndexDesc;

        @SerializedName("highestTemperature")
        @JSONField(name = "highestTemperature")
        String highestTemperature;

        @SerializedName("imageTitleOfDay")
        @JSONField(name = "imageTitleOfDay")
        String imageTitleOfDay;

        @SerializedName("imageTitleOfNight")
        @JSONField(name = "imageTitleOfNight")
        String imageTitleOfNight;

        @SerializedName("lowestTemperature")
        @JSONField(name = "lowestTemperature")
        String lowestTemperature;

        @SerializedName("month")
        @JSONField(name = "month")
        String month;

        @SerializedName("morningExerciseIndex")
        @JSONField(name = "morningExerciseIndex")
        String morningExerciseIndex;

        @SerializedName("morningExerciseIndexDesc")
        @JSONField(name = "morningExerciseIndexDesc")
        String morningExerciseIndexDesc;

        @SerializedName("pm2_5")
        @JSONField(name = "pm2_5")
        String pm25;

        @SerializedName("quality")
        @JSONField(name = "quality")
        String quality;

        @SerializedName("sportIndex")
        @JSONField(name = "sportIndex")
        String sportIndex;

        @SerializedName("sportIndexDesc")
        @JSONField(name = "sportIndexDesc")
        String sportIndexDesc;

        @SerializedName("suggest")
        @JSONField(name = "suggest")
        String suggest;

        @SerializedName("sunBlockIndex")
        @JSONField(name = "sunBlockIndex")
        String sunBlockIndex;

        @SerializedName("sunBlockIndexDesc")
        @JSONField(name = "sunBlockIndexDesc")
        String sunBlockIndexDesc;

        @SerializedName("travelIndex")
        @JSONField(name = "travelIndex")
        String travelIndex;

        @SerializedName("travelIndexDesc")
        @JSONField(name = "travelIndexDesc")
        String travelIndexDesc;

        @SerializedName("umbrellaIndex")
        @JSONField(name = "umbrellaIndex")
        String umbrellaIndex;

        @SerializedName("umbrellaIndexDesc")
        @JSONField(name = "umbrellaIndexDesc")
        String umbrellaIndexDesc;

        @SerializedName("weather")
        @JSONField(name = "weather")
        String weather;

        @SerializedName("wind")
        @JSONField(name = "wind")
        String wind;

        @SerializedName("year")
        @JSONField(name = "year")
        String year;

        public String getCarWashIndex() {
            return this.carWashIndex;
        }

        public String getCarWashIndexDesc() {
            return this.carWashIndexDesc;
        }

        public String getColdIndex() {
            return this.coldIndex;
        }

        public String getColdIndexDesc() {
            return this.coldIndexDesc;
        }

        public String getComfortIndex() {
            return this.comfortIndex;
        }

        public String getComfortIndexDesc() {
            return this.comfortIndexDesc;
        }

        public String getCurrentTemperature() {
            return this.currentTemperature;
        }

        public String getDatingIndex() {
            return this.datingIndex;
        }

        public String getDatingIndexDesc() {
            return this.datingIndexDesc;
        }

        public String getDay() {
            return this.day;
        }

        public String getDayOfWeek() {
            return this.dayOfWeek;
        }

        public String getDressIndex() {
            return this.dressIndex;
        }

        public String getDressIndexDesc() {
            return this.dressIndexDesc;
        }

        public String getDryingIndex() {
            return this.dryingIndex;
        }

        public String getDryingIndexDesc() {
            return this.dryingIndexDesc;
        }

        public String getHighestTemperature() {
            return this.highestTemperature;
        }

        public String getImageTitleOfDay() {
            return this.imageTitleOfDay;
        }

        public String getImageTitleOfNight() {
            return this.imageTitleOfNight;
        }

        public String getLowestTemperature() {
            return this.lowestTemperature;
        }

        public String getMonth() {
            return this.month;
        }

        public String getMorningExerciseIndex() {
            return this.morningExerciseIndex;
        }

        public String getMorningExerciseIndexDesc() {
            return this.morningExerciseIndexDesc;
        }

        public String getPm25() {
            return this.pm25;
        }

        public String getQuality() {
            return this.quality;
        }

        public String getSportIndex() {
            return this.sportIndex;
        }

        public String getSportIndexDesc() {
            return this.sportIndexDesc;
        }

        public String getSuggest() {
            return this.suggest;
        }

        public String getSunBlockIndex() {
            return this.sunBlockIndex;
        }

        public String getSunBlockIndexDesc() {
            return this.sunBlockIndexDesc;
        }

        public String getTravelIndex() {
            return this.travelIndex;
        }

        public String getTravelIndexDesc() {
            return this.travelIndexDesc;
        }

        public String getUmbrellaIndex() {
            return this.umbrellaIndex;
        }

        public String getUmbrellaIndexDesc() {
            return this.umbrellaIndexDesc;
        }

        public String getWeather() {
            return this.weather;
        }

        public String getWind() {
            return this.wind;
        }

        public String getYear() {
            return this.year;
        }

        public void setCarWashIndex(String carWashIndex) {
            this.carWashIndex = carWashIndex;
        }

        public void setCarWashIndexDesc(String carWashIndexDesc) {
            this.carWashIndexDesc = carWashIndexDesc;
        }

        public void setColdIndex(String coldIndex) {
            this.coldIndex = coldIndex;
        }

        public void setColdIndexDesc(String coldIndexDesc) {
            this.coldIndexDesc = coldIndexDesc;
        }

        public void setComfortIndex(String comfortIndex) {
            this.comfortIndex = comfortIndex;
        }

        public void setComfortIndexDesc(String comfortIndexDesc) {
            this.comfortIndexDesc = comfortIndexDesc;
        }

        public void setCurrentTemperature(String currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        public void setDatingIndex(String datingIndex) {
            this.datingIndex = datingIndex;
        }

        public void setDatingIndexDesc(String datingIndexDesc) {
            this.datingIndexDesc = datingIndexDesc;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public void setDressIndex(String dressIndex) {
            this.dressIndex = dressIndex;
        }

        public void setDressIndexDesc(String dressIndexDesc) {
            this.dressIndexDesc = dressIndexDesc;
        }

        public void setDryingIndex(String dryingIndex) {
            this.dryingIndex = dryingIndex;
        }

        public void setDryingIndexDesc(String dryingIndexDesc) {
            this.dryingIndexDesc = dryingIndexDesc;
        }

        public void setHighestTemperature(String highestTemperature) {
            this.highestTemperature = highestTemperature;
        }

        public void setImageTitleOfDay(String imageTitleOfDay) {
            this.imageTitleOfDay = imageTitleOfDay;
        }

        public void setImageTitleOfNight(String imageTitleOfNight) {
            this.imageTitleOfNight = imageTitleOfNight;
        }

        public void setLowestTemperature(String lowestTemperature) {
            this.lowestTemperature = lowestTemperature;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public void setMorningExerciseIndex(String morningExerciseIndex) {
            this.morningExerciseIndex = morningExerciseIndex;
        }

        public void setMorningExerciseIndexDesc(String morningExerciseIndexDesc) {
            this.morningExerciseIndexDesc = morningExerciseIndexDesc;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public void setSportIndex(String sportIndex) {
            this.sportIndex = sportIndex;
        }

        public void setSportIndexDesc(String sportIndexDesc) {
            this.sportIndexDesc = sportIndexDesc;
        }

        public void setSuggest(String suggest) {
            this.suggest = suggest;
        }

        public void setSunBlockIndex(String sunBlockIndex) {
            this.sunBlockIndex = sunBlockIndex;
        }

        public void setSunBlockIndexDesc(String sunBlockIndexDesc) {
            this.sunBlockIndexDesc = sunBlockIndexDesc;
        }

        public void setTravelIndex(String travelIndex) {
            this.travelIndex = travelIndex;
        }

        public void setTravelIndexDesc(String travelIndexDesc) {
            this.travelIndexDesc = travelIndexDesc;
        }

        public void setUmbrellaIndex(String umbrellaIndex) {
            this.umbrellaIndex = umbrellaIndex;
        }

        public void setUmbrellaIndexDesc(String umbrellaIndexDesc) {
            this.umbrellaIndexDesc = umbrellaIndexDesc;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public String getCityName() {
        return this.cityName;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public int getFocusDateIndex() {
        return this.focusDateIndex;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public List<WeatherDay> getWeatherDays() {
        return this.weatherDays;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setFocusDateIndex(int focusDateIndex) {
        this.focusDateIndex = focusDateIndex;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setWeatherDays(List<WeatherDay> weatherDays) {
        this.weatherDays = weatherDays;
    }
}
