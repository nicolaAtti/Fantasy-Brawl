% specMove -- Defines a special move that can be performed by a character
% specMove(@Name,@MoveType,@BaseDamage,@DamageType,@ManaCost,@ModifiersList,@AfflictionsList,@RemovedAfflictions,@NOfTargets)
%
% Author: Nicola Atti

spec_move('Lay On Hands','Spell','Standard',-50,15,[],[],[],1).
spec_move('Censure','Spell','Nothing',0,30,[],['Silenced'],[],1).
spec_move('Holy Smite','Melee','Standard',-30,25,[],[],['Asleep'],1).
spec_move('Bolster Faith','Spell','Nothing',0,40,['Bolstered Faith'],[],[],4).
spec_move('Poison Vial','Ranged','Standard',-20,15,[],['Poisoned'],['Asleep'],1).
spec_move('Flash Grenade','Ranged','Nothing',0,20,[],['Blinded'],['Asleep'],4).
spec_move('Backstab','Melee','Standard',-60,15,[],[],['Asleep'],1).
spec_move('Preparation','Spell','Nothing',0,20,['Prepared'],[],[],1).
spec_move('Second Wind','Spell','Percentage',110,20,[],[],[],1).
spec_move('Sismic Slam','Melee','Standard',-40,15,[],[],['Asleep'],4).
spec_move('Skullcrack','Melee','Standard',-30,20,[],['Stunned'],['Asleep'],1).
spec_move('Berserker Rage','Spell','Nothing',0,30,[],['Berserk'],[],1).
spec_move('Freezing Wind','Spell','Standard',-50,20,[],['Frozen'],['Asleep'],1).
spec_move('Hypnosis','Spell','Nothing',0,30,[],['Asleep'],[],1).
spec_move('Concentration','Spell','Nothing',0,15,['Concentrated'],[],[],1).
spec_move('Flamestrike','Spell','Standard',-40,25,[],[],['Asleep'],4).

spec_move('Shield Bash','Melee','Standard',-10,20,[],['Silenced'],['Asleep'],1).
spec_move('Adrenaline Rush','Spell','Nothing',0,15,['Sped Up'],[],[],1).
spec_move('Iron Will','Spell','Nothing',0,15,['Will Of Iron'],[],[],1).
spec_move('Mighty Slam','Melee','Standard',-40,25,[],[],['Asleep'],1).
spec_move('Trip Attack','Melee','Standard',-15,15,['Slowed'],[],['Asleep'],1).
spec_move('Sunder Defenses','Melee','Standard',-5,15,['Sundered Defences'],[],['Asleep'],1).

spec_move('Chrono Shift','Spell','Percentage',15,30,[],[],['Asleep'],4).
spec_move('Antidote','Spell','Nothing',0,20,[],[],['Poisoned'],1).

% affliction -- Defines the duration in turns for each existing affliction in the game
% affliction(+Name,-TurnDuration)
%
% Author: Nicola Atti

affliction('Poisoned',3).
affliction('Stunned',1).
affliction('Frozen',2).
affliction('Asleep',3).
affliction('Blinded',2).
affliction('Berserk',3).
affliction('Silenced',2).
affliction('Regeneration',2).

%modifier --- Defines the duration,power and the interested attribute for every existing modifier
%modifier(+ModId,-SubStatistic,-TurnDuration,-Value)
%
% Author: Nicola Atti

modifier('Concentrated','Mag-Damage',1,30).
modifier('Prepared','Crit-Chance',1,25).
modifier('Bolstered Faith','Mag-Defence',1,20).
modifier('Sundered Defences','Phys-Defence',2,-20).
modifier('Slowed','Speed',1,-1).
modifier('Will Of Iron','Mag-Defence',2,10).
modifier('Sped Up','Speed',2,2).
