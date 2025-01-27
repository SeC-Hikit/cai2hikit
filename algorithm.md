## Matching Algorithm

In order to find the best match between a trail from cai2hikit and a list from Hikit, we first of all calculate 1 minus the inverse of the square of the delta between the stats metadata of the trails. This gives us a normalized (0..1) and exponential value with 0 being perfect match. The exponent is used to better highlight small distances in data when comparing trails.

Ex. T1 from cai2hikit, T2 from Hikit

```
if((T1.totalRise - T2.totalRise) < 1):
	rise_value = 0
else:
	rise_value = 1 - (1 / ((T1.totalRise - T2.totalRise)^2))
```

The accuracy level is then calculated as the average of all the values:

```
accuracy = (rise_value + fall_value + length_value + eta_value + highest_value + lowest_value) / 6
```

We use this accuracy value to select the best matches and then check the coordinates.