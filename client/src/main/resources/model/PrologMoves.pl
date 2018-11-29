% specMove -- Defines a special move that can be performed by a character
% specMove(@Name,@MoveType,@BaseDamage,@ManaCost,@ModifiersList,@AfflictionsList,@NOfTargets)
%
% Author: Nicola Atti

spec_move('Lay On Hands','Spell',50,15,[],[],1).
spec_move('Censure','Spell',0,30,[],['Silenced'],1).
spec_move('Holy Smite','Melee',30,25,[],[],1).
spec_move('Bolster Faith','Spell',0,40,['Bolstered Faith'],[],4).
spec_move('Poison Vial','Ranged',40,15,[],['Poisoned'],1).
spec_move('Flash Grenade','Ranged',0,20,[],['Blinded'],4).
spec_move('Backstab','Melee',60,15,[],[],1).
spec_move('Preparation','Spell',0,20,['Prepared'],[],1).
spec_move('Second Wind','Spell',0,20,[],[],1).
spec_move('Sismic Slam','Melee',40,15,[],[],4).
spec_move('Skullcrack','Melee',30,20,[],['Stunned'],1).
spec_move('Berserker Rage','Spell',0,30,[],['Berserk'],1).
spec_move('Freezing Wind','Spell',50,20,[],['Frozen'],1).
spec_move('Hypnosis','Spell',0,30,[],['Asleep'],1).
spec_move('Concentration','Spell',0,15,['Concentrated'],[],1).
spec_move('Flamestrike','Spell',40,25,[],[],4).

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

%modifier --- Defines the duration,power and the interested attribute for every existing modifier
%modifier(+ModId,-SubStatistic,-TurnDuration,-Value)
%
% Author: Nicola Atti

modifier('Concentrated','Mag-Damage',1,30).
modifier('Prepared','Crit-Chance',1,25).
modifier('Bolstered Faith','Mag-Defence',1,20).
