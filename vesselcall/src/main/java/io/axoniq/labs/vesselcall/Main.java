package io.axoniq.labs.vesselcall;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Main {
    @Data
    @Builder(toBuilder = true)
    public static class Beer {
        private String name;
        private double percentage;
        private List<String> reviews;


        public Beer setName(String name) {
            return this.toBuilder().name(name).build();
        }
    }

    public static Beer aBeer(String name, double perc, List<String> reviews) {
        return Beer.builder().name(name).percentage(perc).reviews(reviews).build();
    }


    public static List<Beer> beers = asList(
            aBeer("Heineken", 4.5d, asList("Heerlijk", "Helder", "Heineken")),
            aBeer("Artois", 3.5d, asList("Stella Artois...")));

    public static void main(String[] args) {
        System.out.println("Hello");


        List<Beer> newBeers = beers.stream().map(beer -> beer.setName("asdf")).collect(Collectors.toList());

        ArrayList<Beer> beersWithAlcoholAbove4 = new ArrayList<>();
        for (Beer newBeer : newBeers) {
            addOnlyBeersAbove4(beersWithAlcoholAbove4, newBeer);
        }

        List<Beer> stream4 = newBeers.stream()
                .filter(percentageAbove4())
                .distinct()
                .collect(Collectors.toList());

        Function<Beer, Boolean> function = beer -> beer.percentage > 4;
        List<Beer> stream5 = newBeers.stream()
                .filter(beer -> function.apply(beer))
                .distinct()
                .collect(Collectors.toList());

        stream5.stream().forEach(beer -> System.out.println("tada: " + beer));


        Main.beers.forEach(beer -> System.out.println(beer));
        newBeers.forEach(beer -> System.out.println(beer));
    }


    private static void addOnlyBeersAbove4(ArrayList<Beer> beersWithAlcoholAbove4, Beer newBeer) {
        if (newBeer.percentage > 4.0) {
            beersWithAlcoholAbove4.add(newBeer);
        }
    }

    @NotNull
    private static Predicate<Beer> percentageAbove4() {
        return beer -> beer.percentage > 4.0d;
    }
}
