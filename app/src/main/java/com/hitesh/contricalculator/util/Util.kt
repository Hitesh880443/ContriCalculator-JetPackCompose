package com.hitesh.contricalculator.util

/**
 * To calculate the tip
 *
 * @param totalCostValue
 * @param tipPercentage
 * @return
 */
fun calculateTip(totalCostValue: String, tipPercentage: Int): Double {
    if (totalCostValue.isNotEmpty() && totalCostValue.toDouble() > 1) {
        return (totalCostValue.toDouble() * tipPercentage) / 100
    }
    return 0.0
}

/**
 * To calculate the contri amount per person
 *
 * @param splitValue
 * @param totalCostValue
 * @param tipPercentage
 * @return
 */
fun calculatePerPersonCost(splitValue: Int, totalCostValue: String, tipPercentage: Int): Double {
    if (totalCostValue.isNotEmpty() && totalCostValue.toDouble() > 1) {
        return (calculateTip(
            totalCostValue,
            tipPercentage
        ) + totalCostValue.toDouble()) / splitValue
    }
    return 0.0
}
