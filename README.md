asteroids
-
###Classes Needed###
- AsteroidGame - Main class that will represent the game as a whole
- Ship
- Bullets
- Asteroid
- Gravitational Object
	- Only exists in the center of the screen
	- Only effects the players ship
- Alien Ship (potentially extend ship?)
- Rogue Ship (potentially extend ship?)

###Other Details###
- Hitting "esc" brings up options menu which includes:
	- Gravitational Object toggle
		- If on, another toggle for viability
	- Unlimited Lives Mode (Free Play)
	- Number of Asteroids per level
	- Reset high score
	- Continue from saved game
	- Starting level
- Need to figure out a button for pause
	- Continue
	- Save
		- Need to figure out some sort of serialization for saving/loading a game
	- Quit
		- Prompt to make sure they want to quit
- Need to permanently save top 10 scores somehow

