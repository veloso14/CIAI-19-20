import React from 'react';
import ListGroup from "react-bootstrap/ListGroup";
import {Link} from "react-router-dom";

export interface Appointment {
    id: number,
    date: number,
    desc: string,
}

const AppointmentList = (props: { appointments: Appointment[] }) => {


    const {appointments} = props;

    const list = appointments.map((apt: Appointment) => (
        <ListGroup.Item key={apt.id}>
            <Link to={`/appointment/${apt.id}`}>{apt.desc}</Link>
        </ListGroup.Item>)
    );

    let emptyList = (<p className="text-center">You currently don't have any appointments registered!</p>)

    let aptList = ((appointments.length > 0) ? <ListGroup>{list}</ListGroup> : emptyList);

    return (
        <div>
            <h1 className="text-center">Appointments list</h1>
            <br/>
            <ListGroup>{aptList}</ListGroup>
        </div>
    );
};


export default AppointmentList;