<#assign content>

<h1> <br> ${title} <br></h1>

<p>


<textarea id="companyname" name="companyname" rows="1" cols="15"></textarea>
<textarea id="currPrice" name="currPrice" rows="1" cols="15"></textarea>
<textarea id="interest" name="interest" rows="1" cols="15"></textarea>
<textarea id="volatility" name="volatility" rows="1" cols="15"></textarea>
<textarea id="time" name="time" rows="1" cols="15"></textarea>

<div class="row">
  <div class="column">
    <div class="box_header">
      Price Expectations at Expiry Date
    </div>

    <ul name="choices" id="choices" class="list">
      <li>
        <div class="list_element">

          <select name="low" id="low">
            <option value="1">Most Unlikely</option>
            <option value="2">Unlikely</option>
            <option value="3">Neutral</option>
            <option value="4">Likely</option>
            <option value="5">Most Likely</option>
          </select>
          to land at price between
          <textarea id="low_lower" name="low_lower" rows="1" cols="12"></textarea>
          and
          <textarea id="low_upper" name="low_upper" rows="1" cols="12"></textarea>

        </div>
      </li>
      <li>
        <div class="list_element">

          <select name="mid" id="mid">
            <option value="1">Most Unlikely</option>
            <option value="2">Unlikely</option>
            <option value="3">Neutral</option>
            <option value="4">Likely</option>
            <option value="5">Most Likely</option>
          </select>
          to land at price between
          <textarea id="mid_lower" name="mid_lower" rows="1" cols="12"></textarea>
          and
          <textarea id="mid_upper" name="mid_upper" rows="1" cols="12"></textarea>

        </div>
      </li>
      <li>
        <div class="list_element">

          <select name="high" id="high">
            <option value="1">Most Unlikely</option>
            <option value="2">Unlikely</option>
            <option value="3">Neutral</option>
            <option value="4">Likely</option>
            <option value="5">Most Likely</option>
          </select>
          to land at price between
          <textarea id="high_lower" name="high_lower" rows="1" cols="12"></textarea>
          and
          <textarea id="high_upper" name="high_upper" rows="1" cols="12"></textarea>

        </div>
      </li>
    </ul>

    <div class="column_half">
      <div name="add_button_left" id="add_button_left" class="add_button">

      </div>
    </div>

    <div class="column_half">
      <div name="sub_button_left" id="sub_button_left" class="sub_button">

      </div>
    </div>

  </div>

  <div class="column">
    <div class="box_header">
      Investment Portfolio
    </div>

    <ul name="choices" id="choices" class="list">
      <li>
        <div class="list_element">

          <select name="left_left_1" id="left_left_1">
            <option value="Buy">Buy</option>
            <option value="Sell">Sell</option>
          </select>
          <select name="left_middle_2" id="left_middle_2">
            <option value="Call">Call</option>
            <option value="Put">Put</option>
          </select>
          <input type="number" name="Strike" id="Strike" placeholder="0.00" step="0.01" min="0">
          at
          <textarea id="Premium" name="Premium" rows="1" cols="12">Premium</textarea>

        </div>
      </li>
      <li>
        <div class="list_element">

          <select name="left_left_2" id="left_left_2">
            <option value="volvo">Volvo</option>
            <option value="saab">Saab</option>
            <option value="mercedes">Mercedes</option>
            <option value="audi">Audi</option>
          </select>

        </div>
      </li>
      <li>
        <div class="list_element">
          two
        </div>
      </li>
    </ul>

    <div class="column_half">
      <div name="add_button_left" id="add_button_left" class="add_button">

      </div>
    </div>

    <div class="column_half">
      <div name="sub_button_left" id="sub_button_left" class="sub_button">

      </div>
    </div>

  </div>

  <div class="column">
    <div class="box_header">
      News and Analysis
    </div>
  </div>
</div>

<div class="row">
  <button type="button" name="submit" id="submit">Submit</button>
</div>

<div name="error" id="error">

</div>


</p>

</#assign>
<#include "main.ftl">
