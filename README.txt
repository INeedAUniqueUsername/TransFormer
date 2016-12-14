# TransFormer
An experimental system designer for Transcendence

When you start up TransFormer, you will see three empty panels.
The middle panel is the system as it may appear in Transcendence
The center white square represents the <SystemGroup> element. Click an existing element to select it (not implemented yet). Click on nothing to select SystemGroup.
The left panel contains properties for the currently selected element. From top to bottom:
1. The name of the selected element
2. Text fields containing the current values for each of the element's attributes. They can accept a constant [c], range [min]-[max], or dice range [rolls]d[sides][+/-][bonus].
3. The Apply button, which sets the element's attributes to the values specified in their corresponding text fields.
4. The Delete button removes non-<SystemGroup> elements. The currently selected element will be deleted and its parent will be selected.
5. The Create New Sub-Element menu. Here, you can create a new element (such as <Station>) on the currently selected element as long as they are compatible (or else the game will crash upon load).

The right panel holds rendering/other options (currently, there are only two).
1. The Rendering list controls how Orbitals are drawn.
  a. "Draw extremes" draws two forms of the same <Orbitals> from the minimum possible angle to the maximum possible angle. The first form represents the <Orbitals>'s minimum distance from its origin and the second form is for the maximum distance
  b. "Draw distances" draws an instance of the <Orbitals> at every possible distance roll. Useful for showing distance distributions. May be CPU-intensive if you have a dice range like 5d10.
  c. "Draw dots" draws every possible position covered by the <Orbitals> in order to show its position distributions.
  d. "Draw random" (not implemented yet) draws one random, possible form of the system you have made.
2. The Generate button converts the entire system to XML and shows that XML in a dialog box for you to copy and paste into a game file.

PLANNED FEATURES
There is currently no way to handle <Group>, <Primary>, <Siblings>, <Station>, <Table>, or <Label>
A way of handling the "eccentricity" and "count" attributes of <Orbitals>.
Add all the other properties for every element.
