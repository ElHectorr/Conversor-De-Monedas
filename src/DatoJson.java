import com.google.gson.annotations.SerializedName;

import java.util.Map;

public record DatoJson(@SerializedName("conversion_rates") Map<String, Double> conversionRates ) {
}
