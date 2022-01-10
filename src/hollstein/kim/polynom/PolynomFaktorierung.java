package hollstein.kim.polynom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class PolynomFaktorierung {
    ArrayList<PolynomPotenz> faktoren;
    String log;

    public PolynomFaktorierung(ArrayList<PolynomPotenz> faktoren, String log) {
        this.faktoren = faktoren;
        this.log = log;
    }

    public static ArrayList<PolynomPotenz> toPolynomPotenz(@NotNull ArrayList<Polynom> faktoren) {
        ArrayList<PolynomPotenz> polynomFaktoren = (ArrayList<PolynomPotenz>) faktoren.stream()
                .map(polynom -> new PolynomPotenz(polynom, 1))
                .collect(Collectors.toList());

        Collections.sort(polynomFaktoren, new Comparator<PolynomPotenz>() {
            @Override
            public int compare(PolynomPotenz o1, PolynomPotenz o2) {
                int[] p1 = o1.getPolynom().getKoeffizienten();
                int[] p2 = o2.getPolynom().getKoeffizienten();
                if (p1.length > p2.length) {
                    return 1;
                } else if (p1.length < p2.length) {
                    return -1;
                } else {
                    for (int i = p1.length-1; i >= 0; i--) {
                        if (p1[i] > p2[i]) {
                            return 1;
                        } else if (p1[i] < p2[i]) {
                            return -1;
                        }
                    }
                    return 0;
                }
            }
        });
        return polynomFaktoren;
    }
}
