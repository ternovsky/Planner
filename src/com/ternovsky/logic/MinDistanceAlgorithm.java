package com.ternovsky.logic;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 04.11.12
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class MinDistanceAlgorithm {


    public void findShops() {
        /*     for (Product product : shoppingList.getProducts()) {
                Set<Pair<Shop, Product>> shops = productCodeShopsMap.get(product.getCode());
                Pair<String, Set<Pair<Shop, Product>>> pair = new Pair<String, Set<Pair<Shop, Product>>>(
                        product.getCode(), new HashSet<Pair<Shop, Product>>(shops)
                );
                productCodeShopsTreeMap.add(pair);
            }
            int massCenterFirstCoordinate = shoppingList.getFirstCoordinate();
            int firstCoordinateSum = massCenterFirstCoordinate;
            int massCenterSecondCoordinate = shoppingList.getSecondCoordinate();
            int secondCoordinateSum = massCenterSecondCoordinate;
            int coordinatesCount = 1;
            for (Pair<String, Set<Pair<Shop, Product>>> pair : productCodeShopsTreeMap) {
                Set<Pair<Shop, Product>> shopProductPairs = pair.getSecond();
                Pair<Shop, Product> bestShopProductPair = shopProductPairs.iterator().next();
                int bestDistance = distance(massCenterFirstCoordinate, massCenterSecondCoordinate, bestShopProductPair.getFirst().getFirstCoordinate(), bestShopProductPair.getFirst().getSecondCoordinate());
                for (Pair<Shop, Product> shopProductPair : shopProductPairs) {
                    Shop shop = shopProductPair.getFirst();
                    int distance = distance(massCenterFirstCoordinate, massCenterSecondCoordinate, shop.getFirstCoordinate(), shop.getSecondCoordinate());
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestShopProductPair = shopProductPair;
                    }
                }
                Shop bestShop = bestShopProductPair.getFirst();
                addShopProduct(bestShop, bestShopProductPair.getSecond());
                firstCoordinateSum += bestShop.getFirstCoordinate();
                secondCoordinateSum += bestShop.getSecondCoordinate();
                coordinatesCount++;

                massCenterFirstCoordinate = firstCoordinateSum / coordinatesCount;
                massCenterSecondCoordinate = secondCoordinateSum / coordinatesCount;
            }
        }*/
    }

}
