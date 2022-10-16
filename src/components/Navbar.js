import React, {Fragment, useState} from 'react'
import { Link } from 'react-router-dom'


function Navbar() {
  return (
   <Fragment>
    <nav className='navbar'>
      <div className='navbar-container'>
        <Link to="/" className="navbar-logo"></Link>
      </div>
    </nav>
   </Fragment>
  )
}

export default Navbar