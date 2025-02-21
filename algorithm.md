## Matching Algorithm
### Stats Metadata
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
accuracy = (rise_value + fall_value + length_value + highest_value + lowest_value) / 5
```

We use this accuracy value to select the best matches and then check the coordinates.
### Coordinates
The main issue that prevents us from simply comparing the coordinates' values is that no guarantee is given about the sampling frequency of the trail.
A possible solution is to use an algorithm called [Dynamic Type Warping](https://en.wikipedia.org/wiki/Dynamic_time_warping) to warp non-linearly in the time dimension.
The result is a 2d array where the value ```[i][j]``` represents the distance between the first ```i``` coordinates of the first vector and the first ```j``` coordinates of the second with the best alignment.
A few limitations:
- The first coordinates from the first trail must be matched with the first coordinates from the other trail
- The last coordinates from the first trail must be matched with the last coordinates from the other trail

A way to handle it could be to find the best match between the first/last (50?) coordinates of the two trails and set the start/end
of the algorithm accordingly. This approach should help mitigate differences in starting location when recording a track.

The final similarity score is given as the value ```[T1.geometry.size][T2.geometry.size]``` of the DTW array divided by
```(T1.geometry.size + T2.geometry.size) / 2```, which is the average distance between two points of the trail with the best alignment.