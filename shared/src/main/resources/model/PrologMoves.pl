% specMove -- Defines a special move that can be performed by a character
% specMove(@Name,@DamageType,@MoveType,@BaseValue,@ModifiersList,@AlterationsList,@RemovedAlterations,@ManaCost,@NOfTargets)
%
% Author: Nicola Atti

spec_move('Lay On Hands','StandardHeal','Spell',50,[],[],[],15,1).
spec_move('Censure','BuffDebuff','Spell',0,[],['Sil'],[],30,1).
spec_move('Holy Smite','StandardDamage','Melee',30,[],[],['Slp'],25,1).
spec_move('Bolster Faith','BuffDebuff','Spell',0,['Bolstered Faith'],[],[],40,4).
spec_move('Poison Vial','StandardDamage','Ranged',20,[],['Psn'],['Slp'],15,1).
spec_move('Flash Grenade','BuffDebuff','Ranged',0,[],['Bln'],['Slp'],20,4).
spec_move('Backstab','StandardDamage','Melee',60,[],[],['Slp'],15,1).
spec_move('Preparation','BuffDebuff','Spell',0,['Prepared'],[],[],20,1).
spec_move('Second Wind','Percentage','Spell',110,[],[],[],20,1).
spec_move('Sismic Slam','StandardDamage','Melee',40,[],[],['Slp'],15,4).
spec_move('Skullcrack','StandardDamage','Melee',30,[],['Stn'],['Slp'],20,1).
spec_move('Berserker Rage','BuffDebuff','Spell',0,[],['Brk'],[],30,1).
spec_move('Freezing Wind','StandardDamage','Spell',25,[],['Frz'],['Slp'],25,4).
spec_move('Ice Lance','StandardDamage','Spell',40,[],[],['Slp'],20,1).
spec_move('Concentration','BuffDebuff','Spell',0,['Concentrated'],[],[],15,1).
spec_move('Counterspell','BuffDebuff','Spell',0,[],['Sil'],['Slp'],15,1).
spec_move('Shield Bash','StandardDamage','Melee',10,[],['Sil'],['Slp'],20,1).
spec_move('Adrenaline Rush','BuffDebuff','Spell',0,['Sped Up'],[],[],15,1).
spec_move('Iron Will','BuffDebuff','Spell',0,['Will Of Iron'],[],[],15,1).
spec_move('Mighty Slam','StandardDamage','Melee',40,[],[],['Slp'],25,1).
spec_move('Trip Attack','StandardDamage','Melee',15,['Slowed'],[],['Slp'],15,1).
spec_move('Sunder Defenses','StandardDamage','Melee',5,['Sundered Defences'],[],['Slp'],15,1).
spec_move('Chrono Shift','Percentage','Spell',15,[],[],['Slp'],30,4).
spec_move('Haste','BuffDebuff','Spell',0,['Sped Up'],[],[],20,1).
spec_move('Hypnosis','BuffDebuff','Spell',0,[],['Slp'],[],30,1).
spec_move('Arcane Blast','StandardDamage','Spell',50,[],[],['Slp'],25,1).
spec_move('Pyroblast','StandardDamage','Spell',35,[],[],['Slp'],15,1).
spec_move('Dragon Breath','StandardDamage','Spell',15,[],['Stn'],['Slp'],20,1).
spec_move('Flamestrike','StandardDamage','Spell',40,[],[],['Slp'],25,4).
spec_move('Holy Nova','StandardDamage','Spell',30,[],[],['Slp'],20,4).
spec_move('Radiance','Percentage','Spell',130,[],[],[],40,4).
spec_move('Chastise','BuffDebuff','Spell',0,[],['Stn'],['Slp'],20,1).

spec_move('Aimed Shot','StandardDamage','Ranged',35,[],[],['Slp'],15,1).
spec_move('Arrow Volley','StandardDamage','Ranged',20,[],[],['Slp'],25,4).
spec_move('Trueshot','BuffDebuff','Spell',0,['Trueshot'],[],[],20,1).
spec_move('Bear Trap','BuffDebuff','Spell',10,[],['Frz'],['Slp'],15,1).

spec_move('Nature Wrath','StandardDamage','Spell',30,[],[],['Slp'],15,1).
spec_move('Benefic Spores','BuffDebuff','Spell',0,[],['Reg'],[],25,4).
spec_move('Antidote','BuffDebuff','Spell',0,[],[],['Psn'],20,1).
spec_move('Entangling Roots','BuffDebuff','Spell',0,[],['Frz'],[],15,1).
spec_move('Quick Stab','StandardDamage','Melee',30,[],[],['Slp'],15,1).
spec_move('Fan Of Knives','StandardDamage','Ranged',25,[],[],['Slp'],10,4).
spec_move('Duelist Step','BuffDebuff','Spell',0,['Quick Reflexes','Sped Up'],[],[],20,1).
spec_move('Pummel','StandardDamage','Melee',10,[],['Sil'],['Slp'],15,1).


%modifier --- Defines the duration, the power and the interested attribute for every existing modifier
%modifier(+ModId,-SubStatistic,-RoundsDuration,-Value)
%
% Author: Nicola Atti

modifier('Concentrated','MagicalDamage',1,30).
modifier('Prepared','CriticalChance',1,25).
modifier('Bolstered Faith','MagicalDefence',1,20).
modifier('Sundered Defences','PhysicalDefence',2,-20).
modifier('Slowed','Speed',1,-1).
modifier('Will Of Iron','MagicalDefence',2,10).
modifier('Sped Up','Speed',2,2).
modifier('Quick Reflexes','PhysicalDefence',2,10).
modifier('Trueshot','PhysicalCriticalDamage',5,25).