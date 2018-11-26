% specMove -- Defines a special move that can be performed by a character
% specMove(@Name,@DamageAttribute,@DefenceAttribute,@MoveType,
%          @BaseDamage,@ManaCost,@,ModifiersList,@AfflictionsList,@NOfTargets)
%
% Author: Nicola Atti

spec_move('Lay On Hands','Mag_Damage,None','Heal',50,15,[],[],1).
spec_move('Censure','None','None','Spell',0,30,[],['Silenced'],1).
spec_move('Holy Smite','Phys_Damage','Phys_Defence','Melee',30,25,[],[],1).
spec_move('Bolster Faith','None','None','Spell',0,40,['Bolstered Faith'],[],4).
spec_move('Poison Vial','None','None','Ranged',40,15,[],['Poisoned'],1).
spec_move('Flash Grenade','None','None','Ranged',0,20,[],['Blinded'],4).
spec_move('Backstab','Phys_Damage','Phys_Defence','Melee',60,15,[],[],1).
spec_move('Preparation','None','None','Spell',0,20,['Prepared'],[],1).
spec_move('Second Wind','None','None','Spell',0,20,[],[],1).
spec_move('Sismic Slam','Phys_Damage','Phys_Defence','Melee',40,15,[],[],4).
spec_move('Skullcrack','Phys_Damage','Phys_Defence','Melee',30,20,[],['Stunned'],1).
spec_move('Berserker Rage','None','None','Spell',0,999,[],['Berserk'],1).
spec_move('Freezing winds','Mag_Damage','Mag_Defence','Spell',50,20,[],['Frozen'],1).
spec_move('Hypnosis','None','None','Spell',0,30,[],['Asleep'],1).
spec_move('Concentration','None','None','Spell',0,15,['Concentrated'],[],1).
spec_move('Flamestrike','Mag_Damage','Mag_Defence','Spell',40,25,[],[],4).

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
%modifier(+ModId,-Attribute,-TurnDuration,-Value)
%
% Author: Nicola Atti

modifier('Concentrated','Mag Damage',1,30).
modifier('Prepared','Crit Chance',1,25).
modifier('Bolstered Faith','Mag Defence',1,20).