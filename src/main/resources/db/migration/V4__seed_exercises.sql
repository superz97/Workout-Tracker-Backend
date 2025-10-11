-- Chest Exercises
INSERT INTO exercises (name, description, category, muscle_group, instructions) VALUES
('Barbell Bench Press', 'Compound chest exercise performed on a bench with a barbell', 'STRENGTH', 'CHEST',
 'Lie on bench, grip bar slightly wider than shoulders, lower to chest, press up'),
('Dumbbell Flyes', 'Isolation exercise for chest using dumbbells', 'STRENGTH', 'CHEST',
 'Lie on bench, hold dumbbells above chest, lower in arc motion, return to start'),
('Push-ups', 'Bodyweight exercise for chest, shoulders, and triceps', 'CALISTHENICS', 'CHEST',
 'Start in plank position, lower body until chest nearly touches floor, push back up'),
('Incline Dumbbell Press', 'Upper chest focused pressing movement', 'STRENGTH', 'CHEST',
 'Set bench to 30-45 degrees, press dumbbells from chest level to above'),

-- Back Exercises
('Pull-ups', 'Bodyweight exercise for back and biceps', 'CALISTHENICS', 'BACK',
 'Hang from bar, pull body up until chin over bar, lower with control'),
('Barbell Row', 'Compound back exercise with barbell', 'STRENGTH', 'BACK',
 'Hinge at hips, pull bar to lower chest, squeeze shoulder blades'),
('Lat Pulldown', 'Cable exercise targeting latissimus dorsi', 'STRENGTH', 'LATS',
 'Sit at machine, pull bar to upper chest, control on way up'),
('Deadlift', 'Full body compound exercise', 'POWERLIFTING', 'BACK',
 'Stand with feet hip-width, grip bar, lift by extending hips and knees'),

-- Shoulder Exercises
('Overhead Press', 'Compound shoulder exercise with barbell', 'STRENGTH', 'SHOULDERS',
 'Press bar from shoulder level to overhead, keep core tight'),
('Lateral Raises', 'Isolation exercise for side delts', 'STRENGTH', 'SHOULDERS',
 'Hold dumbbells at sides, raise to shoulder height, lower with control'),
('Face Pulls', 'Rear delt and upper back exercise', 'STRENGTH', 'SHOULDERS',
 'Pull rope to face level, separate hands at end, focus on rear delts'),
('Arnold Press', 'Variation of shoulder press hitting all heads', 'STRENGTH', 'SHOULDERS',
 'Start with palms facing you, rotate while pressing overhead'),

-- Leg Exercises
('Barbell Squat', 'Compound leg exercise', 'STRENGTH', 'QUADRICEPS',
 'Bar on upper back, squat down keeping chest up, drive through heels'),
('Romanian Deadlift', 'Hamstring and glute focused exercise', 'STRENGTH', 'HAMSTRINGS',
 'Hold bar at hips, hinge keeping legs slightly bent, feel stretch in hamstrings'),
('Leg Press', 'Machine-based compound leg exercise', 'STRENGTH', 'QUADRICEPS',
 'Sit in machine, press weight through full range of motion'),
('Walking Lunges', 'Unilateral leg exercise', 'STRENGTH', 'GLUTES',
 'Step forward into lunge, alternate legs while walking'),
('Calf Raises', 'Isolation exercise for calves', 'STRENGTH', 'CALVES',
 'Rise up on toes, hold briefly, lower with control'),

-- Arms Exercises
('Barbell Curl', 'Bicep isolation exercise', 'STRENGTH', 'BICEPS',
 'Hold bar with underhand grip, curl to chest, lower with control'),
('Hammer Curls', 'Bicep and forearm exercise', 'STRENGTH', 'BICEPS',
 'Hold dumbbells with neutral grip, curl without rotating wrists'),
('Tricep Dips', 'Bodyweight tricep exercise', 'CALISTHENICS', 'TRICEPS',
 'Support body on bars, lower by bending elbows, press back up'),
('Overhead Tricep Extension', 'Tricep isolation with dumbbell', 'STRENGTH', 'TRICEPS',
 'Hold dumbbell overhead, lower behind head, extend back up'),

-- Core Exercises
('Plank', 'Isometric core exercise', 'CALISTHENICS', 'ABS',
 'Hold push-up position on forearms, keep body straight'),
('Russian Twists', 'Oblique focused core exercise', 'STRENGTH', 'OBLIQUES',
 'Sit with knees bent, rotate torso side to side with weight'),
('Hanging Leg Raises', 'Advanced ab exercise', 'CALISTHENICS', 'ABS',
 'Hang from bar, raise legs to 90 degrees or higher'),
('Ab Wheel Rollout', 'Advanced core exercise', 'STRENGTH', 'ABS',
 'Kneel with wheel, roll forward maintaining neutral spine, roll back'),

-- Cardio Exercises
('Running', 'Cardiovascular exercise', 'CARDIO', 'FULL_BODY',
 'Maintain steady pace, focus on breathing and form'),
('Cycling', 'Low-impact cardio exercise', 'CARDIO', 'FULL_BODY',
 'Maintain consistent cadence, adjust resistance as needed'),
('Burpees', 'Full body cardio and strength exercise', 'PLYOMETRIC', 'FULL_BODY',
 'Squat, jump back to plank, push-up, jump feet to hands, jump up'),
('Jump Rope', 'Cardio exercise with rope', 'CARDIO', 'FULL_BODY',
 'Jump with both feet, rotate rope with wrists, maintain rhythm'),
('Mountain Climbers', 'Core and cardio exercise', 'PLYOMETRIC', 'FULL_BODY',
 'Start in plank, alternate bringing knees to chest rapidly'),

-- Olympic Lifts
('Clean and Press', 'Olympic compound movement', 'OLYMPIC', 'FULL_BODY',
 'Clean bar to shoulders, then press overhead in one fluid motion'),
('Snatch', 'Olympic lift from floor to overhead', 'OLYMPIC', 'FULL_BODY',
 'Pull bar from floor to overhead in one explosive movement'),

-- Additional Exercises
('Box Jumps', 'Plyometric leg exercise', 'PLYOMETRIC', 'QUADRICEPS',
 'Jump onto box, step down, repeat with explosive power'),
('Cable Crossover', 'Chest isolation with cables', 'STRENGTH', 'CHEST',
 'Pull cables from high to low in arc motion, squeeze chest'),
('T-Bar Row', 'Back exercise with T-bar', 'STRENGTH', 'BACK',
 'Pull bar to chest while bent over, squeeze shoulder blades'),
('Hip Thrusts', 'Glute-focused exercise', 'STRENGTH', 'GLUTES',
 'Back on bench, bar on hips, thrust hips up squeezing glutes'),
('Farmers Walk', 'Full body carry exercise', 'STRENGTH', 'FULL_BODY',
 'Hold heavy weights at sides, walk maintaining upright posture');